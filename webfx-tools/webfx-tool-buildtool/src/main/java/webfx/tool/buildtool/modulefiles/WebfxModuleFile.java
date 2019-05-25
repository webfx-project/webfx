package webfx.tool.buildtool.modulefiles;

import webfx.tool.buildtool.ModuleDependency;
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

    public boolean isExecutable() {
        return getBooleanAttributeValue(getDocument().getDocumentElement(), "executable");
    }

    public boolean isInterface() {
        return getBooleanAttributeValue(getDocument().getDocumentElement(), "interface");
    }

    public String implementingInterface() {
        return getAttributeValue(getDocument().getDocumentElement(), "implements-module");
    }

    public ReusableStream<ModuleDependency> getSourceModuleDependencies() {
        return lookupDependencies("/module/dependencies/source-modules//module", ModuleDependency.Type.SOURCE);
    }

    public ReusableStream<ModuleDependency> getPluginModuleDependencies() {
        return lookupDependencies("/module/dependencies/plugin-modules//module", ModuleDependency.Type.PLUGIN);
    }

    public ReusableStream<ModuleDependency> getResourceModuleDependencies() {
        return lookupDependencies("/module/dependencies/resource-modules//module", ModuleDependency.Type.RESOURCE);
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

    public ReusableStream<String> getArrayNewInstanceClasses() {
        return lookupNodeListTextContent("/module/reflect/array-new-instance//class");
    }

    public ReusableStream<String> providedJavaServices() {
        return lookupNodeListAttribute("/module/providers//provider", "spi").distinct();
    }

    public ReusableStream<String> providedJavaServicesProviders(String javaService) {
        return lookupNodeListTextContent("/module/providers//provider[@spi='" + javaService + "']");
    }
}
