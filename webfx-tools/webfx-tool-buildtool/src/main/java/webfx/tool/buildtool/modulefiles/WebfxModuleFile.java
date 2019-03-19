package webfx.tool.buildtool.modulefiles;

import webfx.tool.buildtool.ProjectModule;
import webfx.tool.buildtool.util.reusablestream.ReusableStream;

import java.nio.file.Path;

/**
 * @author Bruno Salmon
 */
public final class WebfxModuleFile extends XmlModuleFile {

    public WebfxModuleFile(ProjectModule module) {
        super(module, true);
    }

    Path getModulePath() {
        return resolveFromModuleHomeDirectory("src/main/resources/META-INF/webfx.xml");
    }

    public ReusableStream<webfx.tool.buildtool.Module> getSourceModules() {
        return lookupModules("/module/dependencies/source-modules//module");
    }

    public ReusableStream<webfx.tool.buildtool.Module> getPluginModules() {
        return lookupModules("/module/dependencies/plugin-modules//module");
    }

    public ReusableStream<webfx.tool.buildtool.Module> getResourceModules() {
        return lookupModules("/module/dependencies/resource-modules//module");
    }

    public ReusableStream<webfx.tool.buildtool.Module> getOptionalModules() {
        return lookupModules("/module/dependencies/optional-modules//module");
    }

    public ReusableStream<String> getResourcePackages() {
        return lookupNodeListTextContent("/module/resource-packages//package");
    }

    public ReusableStream<String> getEmbedResources() {
        return lookupNodeListTextContent("/module/embed-resources//resource");
    }

    public ReusableStream<String> getSystemProperties() {
        return lookupNodeListTextContent("/module/system-properties//property");
    }

}
