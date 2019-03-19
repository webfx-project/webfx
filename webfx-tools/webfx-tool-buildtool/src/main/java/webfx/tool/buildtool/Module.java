package webfx.tool.buildtool;

/**
 * @author Bruno Salmon
 */
public interface Module extends Comparable<Module> {

    String getGroupId();

    String getArtifactId();

    String getVersion();

    static Module create(String artifactId) {
        return new ModuleImpl(artifactId);
    }

    @Override
    default int compareTo(Module m) {
        return getArtifactId().compareTo(m.getArtifactId());
    }
}
