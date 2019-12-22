package webfx.tools.buildtool;

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
        // Moving JavaFx emulation modules on top (before JavaFx itself even if scope is just provided) so specific emulation API can be eventually be used in peer java code
        boolean thisEmul = RootModule.isJavaFxEmulModule(this);
        boolean mEmul = RootModule.isJavaFxEmulModule(m);
        if (thisEmul != mEmul)
            return thisEmul ? -1 : 1;
        // This (temporary) rule is just for GridCollator (which has a different implementation in gwt so must be listed first)
        String GridCollatorPeerPrefix = "webfx-extras-visual-controls-grid-peers";
        if (getName().startsWith(GridCollatorPeerPrefix) && m.getName().startsWith(GridCollatorPeerPrefix)) {
            if (getName().endsWith("-gwt"))
                return -1;
            if (m.getName().endsWith("-gwt"))
                return 1;
        }
        // Everything else is sorted by alphabetic order
        return getName().compareTo(m.getName());
    }
}
