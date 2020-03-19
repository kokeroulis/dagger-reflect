/*
 * Copyright (C) 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dagger.reflect;

import dagger.MembersInjector;
import dagger.reflect.Binding.LinkedBinding;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static dagger.reflect.Reflection.*;

final class ReflectiveMembersInjector<T> implements MembersInjector<T> {
  static <T> MembersInjector<T> create(Class<T> cls, Scope scope) {


    /**
     * [P, com.example.ExamplePresenter]
     */
    ConcurrentHashMap<String, Class<?>> genericParameterNameAndActualClass = new ConcurrentHashMap<>();
    Deque<ClassInjector<T>> classInjectors = new ArrayDeque<>();
    Class<?> target = cls;

    while (target != Object.class && target != null) {

      if (TypeUtil.classHasGenericParameter(target)) {
        ParameterizedType baseClass = (ParameterizedType) target.getGenericSuperclass();

        if (baseClass.getRawType() instanceof Class<?>) {
          Class<?> rawType = (Class<?>) baseClass.getRawType();

          for (int typeParameterIndex = 0; typeParameterIndex < rawType.getTypeParameters().length; typeParameterIndex++) {
            // TODO save the array on a list for faster iteration?
            TypeVariable<? extends Class<?>> typeParameter = rawType.getTypeParameters()[typeParameterIndex];
            String parameterTypeName = Key.getTypeName(typeParameter);

            // TODO save the array on a list for faster iteration?
            // TODO could baseClass size be different from rawType?!?!?!
            Type parameterActualType = baseClass.getActualTypeArguments()[typeParameterIndex];
            appendActualParamterType(genericParameterNameAndActualClass, parameterTypeName, parameterActualType);
          }
        }
      }
      Map<Field, LinkedBinding<?>> fieldBindings = new LinkedHashMap<>();
      for (Field field : target.getDeclaredFields()) {
        if (field.getAnnotation(Inject.class) == null) {
          continue;
        }
        if (Modifier.isPrivate(field.getModifiers())) {
          throw new IllegalArgumentException(
              "Dagger does not support injection into private fields: "
                  + target.getCanonicalName()
                  + "."
                  + field.getName());
        }
        if (Modifier.isStatic(field.getModifiers())) {
          throw new IllegalArgumentException(
              "Dagger does not support injection into static fields: "
                  + target.getCanonicalName()
                  + "."
                  + field.getName());
        }

        String keyName = TypeUtil.findGenericTypeFromDelegateClass(field);
        if (keyName == null) {
          keyName = Key.getTypeName(field.getGenericType());
        }
        Key candidateKey;
        if (genericParameterNameAndActualClass.containsKey(keyName)) {
          Type actualType = substitudeGenericErasedType(field, genericParameterNameAndActualClass);
          candidateKey = Key.of(
                  findQualifier(field.getDeclaredAnnotations()),
                  actualType != null ? actualType : field.getGenericType()
          );
        } else {
          candidateKey = Key.of(findQualifier(field.getDeclaredAnnotations()), field.getGenericType());
        }

        LinkedBinding<?> binding = BenchmarkUtil.measure("ReflectiveAMemberInjector.inject " + " target " + target + candidateKey.toString(), () -> {
          return scope.getBinding(candidateKey);
        });

        fieldBindings.put(field, binding);
      }

      Map<Method, LinkedBinding<?>[]> methodBindings = new LinkedHashMap<>();
      for (Method method : target.getDeclaredMethods()) {
        if (method.getAnnotation(Inject.class) == null) {
          continue;
        }
        if (Modifier.isPrivate(method.getModifiers())) {
          throw new IllegalArgumentException(
              "Dagger does not support injection into private methods: "
                  + target.getCanonicalName()
                  + "."
                  + method.getName()
                  + "()");
        }
        if (Modifier.isStatic(method.getModifiers())) {
          throw new IllegalArgumentException(
              "Dagger does not support injection into static methods: "
                  + target.getCanonicalName()
                  + "."
                  + method.getName()
                  + "()");
        }
        if (Modifier.isAbstract(method.getModifiers())) {
          throw new IllegalArgumentException(
              "Methods with @Inject may not be abstract: "
                  + target.getCanonicalName()
                  + "."
                  + method.getName()
                  + "()");
        }

        Type[] parameterTypes = method.getGenericParameterTypes();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        LinkedBinding<?>[] bindings = new LinkedBinding<?>[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
          Key key = Key.of(findQualifier(parameterAnnotations[i]), parameterTypes[i]);

          bindings[i] = BenchmarkUtil.measure("for lopp " + " target " + target + key.toString(), () -> {
            return scope.getBinding(key);
          });
        }

        methodBindings.put(method, bindings);
      }

      if (!fieldBindings.isEmpty() || !methodBindings.isEmpty()) {
        // Per JSR 330, fields and methods in superclasses are injected before those in subclasses.
        // We are traversing upward in the class hierarchy so each injector is prepended to the
        // collection to ensure regular iteration will honor this contract.
        classInjectors.addFirst(new ClassInjector<>(fieldBindings, methodBindings));
      }

      target = target.getSuperclass();
    }

    return new ReflectiveMembersInjector<>(classInjectors);
  }

  static void appendActualParamterType(
          Map<String, Class<?>> genericParameterNameAndActualClass,
          String parameterTypeName,
          Type parameterActualType
  ) {
    if (parameterActualType instanceof Class<?>) {
      genericParameterNameAndActualClass.put(parameterTypeName, (Class<?>) parameterActualType);
    } else if (parameterActualType instanceof TypeVariable) {
      String parameterActualTypeName = Key.getTypeName(parameterActualType);
      Class<?> parameterizedClassCandidate = genericParameterNameAndActualClass.get(parameterActualTypeName);

      if (parameterizedClassCandidate != null) {
        genericParameterNameAndActualClass.put(parameterTypeName, parameterizedClassCandidate);
      }
    }
  }

  @Nullable
  static Type substitudeGenericErasedType(Field field, ConcurrentHashMap<String, Class<?>> genericParameterNameAndActualClass) {
    Type genericType = field.getGenericType();

    if (genericType instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) genericType;

      // TODO check if we need this if... probably not!
      if (parameterizedType.getActualTypeArguments().length > 0) {
        Class<?>[] actualParameterTypeArray = new Class<?>[parameterizedType.getActualTypeArguments().length];

        for (int parameterIndex = 0; parameterIndex < parameterizedType.getActualTypeArguments().length; parameterIndex++) {
          final Type parameterType = parameterizedType.getActualTypeArguments()[parameterIndex];
          final String parameterTypeName = Key.getTypeName(parameterType);
          final Class<?> actualParameterType = genericParameterNameAndActualClass.get(parameterTypeName);

          if (actualParameterType != null) {
            actualParameterTypeArray[parameterIndex] = actualParameterType;
          }
        }

        return new TypeUtil.ParameterizedTypeImpl(null, parameterizedType.getRawType(), actualParameterTypeArray);
      }
    }

    return null;
  }

  private final Iterable<ClassInjector<T>> classInjectors;

  private ReflectiveMembersInjector(Iterable<ClassInjector<T>> classInjectors) {
    this.classInjectors = classInjectors;
  }

  @Override
  public void injectMembers(T instance) {
    for (ClassInjector<T> classInjector : classInjectors) {
      classInjector.injectMembers(instance);
    }
  }

  private static final class ClassInjector<T> implements MembersInjector<T> {
    final Map<Field, LinkedBinding<?>> fieldBindings;
    final Map<Method, LinkedBinding<?>[]> methodBindings;

    ClassInjector(
        Map<Field, LinkedBinding<?>> fieldBindings,
        Map<Method, LinkedBinding<?>[]> methodBindings) {
      this.fieldBindings = fieldBindings;
      this.methodBindings = methodBindings;
    }

    @Override
    public void injectMembers(T instance) {
      // Per JSR 330, fields are injected before methods.
      for (Map.Entry<Field, LinkedBinding<?>> fieldBinding : fieldBindings.entrySet()) {
        trySet(instance, fieldBinding.getKey(), fieldBinding.getValue().get());
      }
      for (Map.Entry<Method, LinkedBinding<?>[]> methodBinding : methodBindings.entrySet()) {
        LinkedBinding<?>[] bindings = methodBinding.getValue();
        Object[] arguments = new Object[bindings.length];
        for (int i = 0; i < bindings.length; i++) {
          arguments[i] = bindings[i].get();
        }
        tryInvoke(instance, methodBinding.getKey(), arguments);
      }
    }
  }
}
