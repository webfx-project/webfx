package webfx.tools.buildtool.modulefiles;

import webfx.tools.buildtool.ProjectModule;

import java.io.File;
import java.nio.file.Path;

/**
 * @author Bruno Salmon
 */
abstract class ModuleFile {

    private final ProjectModule module;

    ModuleFile(ProjectModule module) {
        this.module = module;
    }

    public ProjectModule getModule() {
        return module;
    }

    Path resolveFromModuleHomeDirectory(String relativePath) {
        return getModule().getHomeDirectory().resolve(relativePath);
    }

    abstract Path getModulePath();

    File getModuleFile() {
        return getModulePath().toFile();
    }

    abstract void readFile();

    abstract void writeFile();

}
