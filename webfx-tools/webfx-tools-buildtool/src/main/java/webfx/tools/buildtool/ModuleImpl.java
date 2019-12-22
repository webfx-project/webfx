package webfx.tools.buildtool;

/**
 * @author Bruno Salmon
 */
class ModuleImpl implements Module {

    private final String name;

    ModuleImpl(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModuleImpl)) return false;

        ModuleImpl module = (ModuleImpl) o;

        return name.equals(module.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
