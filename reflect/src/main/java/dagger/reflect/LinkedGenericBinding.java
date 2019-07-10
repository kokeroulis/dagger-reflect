package dagger.reflect;

import dagger.reflect.Binding.LinkedBinding;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@IgnoreJRERequirement // Only linked when requested by user code referencing j.u.Optional.
final class LinkedGenericBinding<T> extends LinkedBinding<T> {
  private final LinkedBinding<T> dependency;

  LinkedGenericBinding(LinkedBinding<T> dependency) {
    this.dependency = dependency;
  }

  @Override
  public T get() {
    T value = dependency.get();
    if (value == null) {
      throw new NullPointerException(
          dependency + " returned null which is not allowed for non optional bindings");
    }

    return value;
  }

  @Override
  public String toString() {
    return "@BindsGenericErasure[" + dependency + ']';
  }
}
