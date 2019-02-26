package webfx.tool.buildtool.modulefiles;

import webfx.tool.buildtool.ProjectModule;

import java.nio.file.Path;

/**
 * @author Bruno Salmon
 */
public final class GwtModuleFile extends XmlModuleFile {

    public GwtModuleFile(ProjectModule module) {
        super(module);
    }

    @Override
    Path getModulePath() {
        return resolveFromModuleHomeDirectory("src/main/module.gwt.xml");
    }
}
