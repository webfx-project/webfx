package webfx.tool.buildtool;

/**
 * @author Bruno Salmon
 */
public interface Module extends Comparable<Module> {

    String getName();

    static Module create(String artifactId) {
        return new ModuleImpl(artifactId);
    }

    @Override
    default int compareTo(Module m) {
        return getName().compareTo(m.getName());
    }
}
