package webfx.tool.buildhelper;

/**
 * @author Bruno Salmon
 */
class ModuleImpl implements Module {

    private final String groupId;
    private final String artifactId;
    private final String version;

    ModuleImpl(String artifactId) {
        this(null, artifactId);
    }

    ModuleImpl(String groupId, String artifactId) {
        this(groupId, artifactId, null);
    }

    ModuleImpl(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public String getArtifactId() {
        return artifactId;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return getArtifactId();
    }
}
