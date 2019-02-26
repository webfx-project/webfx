package webfx.tool.buildtool;

/**
 * @author Bruno Salmon
 */
public interface Module {

    String getGroupId();

    String getArtifactId();

    String getVersion();

    static Module create(String artifactId) {
        return new ModuleImpl(artifactId);
    }

}
