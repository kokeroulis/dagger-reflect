package dagger.reflect;

public class ReusableUnLinkedBinding extends Binding.UnlinkedBinding {

    private final Binding unlinkedBinding;

    public ReusableUnLinkedBinding(Binding unlinkedBinding) {
        this.unlinkedBinding = unlinkedBinding;
    }

    @Override
    public LinkedBinding<?> link(Linker linker, Scope scope) {
        LinkedBinding<?> linkedBinding = unlinkedBinding.link(linker, scope);
        return new ReusableLinkedBinding<>(linkedBinding);
    }
}
