package dagger.reflect;

import dagger.reflect.Binding.LinkedBinding;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;

import static dagger.reflect.Reflection.tryInvoke;

@SuppressWarnings("UnusedVariable")
public final class LinkedMultiMapBinding<T> extends LinkedBinding<T> {
  private final @Nullable Object instance;
  private final Method method;
  private final LinkedBinding<?>[] dependencies;

  LinkedMultiMapBinding(@Nullable Object instance, Method method, LinkedBinding<?>[] dependencies) {
    this.instance = instance;
    this.method = method;
    this.dependencies = dependencies;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @Nullable T get() {
    return (T) Collections.emptyMap();
  }

  @Override
  public String toString() {
    return "@Provides[" + method.getDeclaringClass().getName() + '.' + method.getName() + "(…)]";
  }
}
