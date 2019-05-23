package webfx.tool.buildtool;

/**
 * @author Bruno Salmon
 */
public interface Module extends Comparable<Module> {

    String getName();

    static Module create(String artifactId) {
        return new ModuleImpl(artifactId);
    }

    // Comparison function used to sort modules dependencies in the Maven pom files
    @Override
    default int compareTo(Module m) {
        // Moving JavaFx emulation modules on top so any reference to JavaFX API
        boolean thisEmul = RootModule.isJavaFxEmulModule(this);
        boolean mEmul = RootModule.isJavaFxEmulModule(m);
        if (thisEmul != mEmul)
            return thisEmul ? -1 : 1;
        // Everything else is sorted by alphabetic order
        return getName().compareTo(m.getName());
    }
}
