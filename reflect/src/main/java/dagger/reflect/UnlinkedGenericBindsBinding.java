package dagger.reflect;

import dagger.reflect.Binding.UnlinkedBinding;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static dagger.reflect.Reflection.findQualifier;

final class UnlinkedGenericBindsBinding extends UnlinkedBinding {
    private final Method method;
    private final Class<?> classForGeneric;

    UnlinkedGenericBindsBinding(Method method, Class<?> classForGeneric) {
        this.method = method;
        this.classForGeneric = classForGeneric;
    }

    @Override
    public LinkedBinding<?> link(Linker linker, Scope scope) {
        Type[] parameterTypes = method.getGenericParameterTypes();
        if (parameterTypes.length != 1) {
            throw new IllegalArgumentException("@Binds methods must have a single parameter: " + method);
        }

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Key key = Key.of(findQualifier(parameterAnnotations[0]), classForGeneric);
        LinkedBinding<?> dependency = linker.find(key);
        if (dependency == null) {
            throw new RuntimeException("we couldn't find the dependency");
        }
        return new LinkedGenericBinding<>(dependency);
    }

    @Override
    public String toString() {
        return "@Binds[" + method.getDeclaringClass().getName() + '.' + method.getName() + "(…)]";
    }
}
