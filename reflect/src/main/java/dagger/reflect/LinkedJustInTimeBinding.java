package dagger.reflect;

import static dagger.reflect.Reflection.tryInstantiate;

import dagger.MembersInjector;
import dagger.reflect.Binding.LinkedBinding;
import java.lang.reflect.Constructor;
import java.util.Map;

public final class LinkedJustInTimeBinding<T> extends LinkedBinding<T> {
  private final Constructor<T> constructor;
  private final LinkedBinding<?>[] dependencies;
  private final MembersInjector<T> membersInjector;
  private boolean isRecyclable;

  LinkedJustInTimeBinding(
      Constructor<T> constructor,
      LinkedBinding<?>[] dependencies,
      MembersInjector<T> membersInjector) {
    this.constructor = constructor;
    this.dependencies = dependencies;
    this.membersInjector = membersInjector;
    this.isRecyclable = true;
  }

  @Override
  public boolean isRecyclable() {
    return isRecyclable;
  }

  @Override
  public T get() {
    Object[] arguments = new Object[dependencies.length];
    for (int i = 0; i < dependencies.length; i++) {
      arguments[i] = dependencies[i].get();
      if (isRecyclable) {
        isRecyclable = !(arguments[i] instanceof Map);
      }
    }
    T instance = tryInstantiate(constructor, arguments);
    membersInjector.injectMembers(instance);
    return instance;
  }

  @Override
  public String toString() {
    return "@Inject[" + constructor.getDeclaringClass().getName() + ".<init>(…)]";
  }
}
