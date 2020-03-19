package dagger.reflect;

import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

public class ReusableLinkedBinding<T> extends Binding.LinkedBinding<T> {

    private final LinkedBinding<T> binding;
    @Nullable
    private WeakReference<T> value;

    public ReusableLinkedBinding(LinkedBinding<T> binding) {
        this.binding = binding;
    }

    @Override
    public T get() {
        if (value == null || value.get() == null) {
            T bindingValue = binding.get();
            value = new WeakReference<>(bindingValue);
            return bindingValue;
        } else {
            return value.get();
        }
    }
}
