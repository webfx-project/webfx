package webfx.tool.buildtool.modulefiles;

import webfx.tool.buildtool.ProjectModule;

import java.nio.file.Path;

/**
 * @author Bruno Salmon
 */
public final class MavenModuleFile extends XmlModuleFile {

    public MavenModuleFile(ProjectModule module) {
        super(module);
    }

    @Override
    Path getModulePath() {
        return resolveFromModuleHomeDirectory("pom.xml");
    }
}
