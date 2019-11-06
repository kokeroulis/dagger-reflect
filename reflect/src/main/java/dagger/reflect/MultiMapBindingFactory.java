package dagger.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

final class MultiMapBindingFactory {

    final Key key;
    final Annotation[] annotations;
    final Binding.UnlinkedBinding binding;
    final Type returnType;

    MultiMapBindingFactory(Key key, Annotation[] annotations, Binding.UnlinkedBinding binding, Type returnType) {
        this.key = key;
        this.annotations = annotations;
        this.binding = binding;
        this.returnType = returnType;
    }

    boolean isBindingAllowed(Scope.Builder scopeBuilder) {
        return !scopeBuilder.hasBinding(key);
    }
}