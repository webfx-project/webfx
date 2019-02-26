package webfx.tool.buildtool.modulefiles;

import webfx.tool.buildtool.ProjectModule;

import java.nio.file.Path;

/**
 * @author Bruno Salmon
 */
public final class WebfxModuleFile extends XmlModuleFile {

    public WebfxModuleFile(ProjectModule module) {
        super(module);
    }

    Path getModulePath() {
        return resolveFromModuleHomeDirectory("src/main/resources/META-INF/webfx.xml");
    }

}
