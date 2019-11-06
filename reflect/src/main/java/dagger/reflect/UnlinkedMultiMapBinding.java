package dagger.reflect;

import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static dagger.reflect.Reflection.findQualifier;

final class UnlinkedMultiMapBinding extends Binding.UnlinkedBinding {
  private final @Nullable Object instance;
  private final Method method;

  UnlinkedMultiMapBinding(@Nullable Object instance, Method method) {
    this.instance = instance;
    this.method = method;
  }

  @Override
  public LinkedBinding<?> link(Linker linker, Scope scope) {
    return new LinkedMultiMapBinding<>(instance, method, new LinkedBinding<?>[] {});
  }

  @Override
  public String toString() {
    return "@Binds Multimap[" + method.getDeclaringClass().getName() + '.' + method.getName() + "(…)]";
  }
}
