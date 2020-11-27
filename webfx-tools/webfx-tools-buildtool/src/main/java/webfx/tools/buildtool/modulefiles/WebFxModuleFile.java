package webfx.tools.buildtool.modulefiles;

import org.w3c.dom.Node;
import webfx.tools.buildtool.ModuleDependency;
import webfx.tools.buildtool.ProjectModule;
import webfx.tools.util.reusablestream.ReusableStream;
import webfx.tools.buildtool.util.xml.XmlUtil;

import java.nio.file.Path;

/**
 * @author Bruno Salmon
 */
public final class WebFxModuleFile extends XmlModuleFile {

    public WebFxModuleFile(ProjectModule module) {
        super(module, true);
    }

    Path getModulePath() {
        return resolveFromModuleHomeDirectory("src/main/resources/META-INF/webfx.xml");
    }

    public boolean isExecutable() {
        return getBooleanModuleAttributeValue("executable");
    }

    public boolean isInterface() {
        return getBooleanModuleAttributeValue("interface");
    }

    public boolean isAutomatic() {
        return getBooleanModuleAttributeValue("automatic");
    }

    public String implementingInterface() {
        return getModuleAttributeValue("implements-module");
    }

    public String getModuleProperty(String property) {
        return getModuleAttributeValue(property);
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

    public ReusableStream<String> getRequiredPackages() {
        return lookupNodeListTextContent("/module/required-conditions//if-uses-java-package");
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

    public String getGraalVmReflectionJson() {
        return lookupNodeTextContent("/module/graalvm-reflection-json");
    }

    public ReusableStream<String> providedJavaServices() {
        return lookupNodeListAttribute("/module/providers//provider", "spi").distinct();
    }

    public ReusableStream<String> providedJavaServicesProviders(String javaService) {
        return lookupNodeListTextContent("/module/providers//provider[@spi='" + javaService + "']");
    }

    public Node getHtmlNode() {
        return lookupNode("/module/html");
    }

    private boolean getBooleanModuleAttributeValue(String attribute) {
        return XmlUtil.getBooleanAttributeValue(getDocument().getDocumentElement(), attribute);
    }

    private String getModuleAttributeValue(String attribute) {
        return XmlUtil.getAttributeValue(getDocument().getDocumentElement(), attribute);
    }
}
