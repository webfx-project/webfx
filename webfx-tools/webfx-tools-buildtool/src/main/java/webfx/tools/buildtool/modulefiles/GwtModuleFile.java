package webfx.tools.buildtool.modulefiles;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import webfx.tools.buildtool.Module;
import webfx.tools.buildtool.ModuleDependency;
import webfx.tools.buildtool.ProjectModule;
import webfx.tools.buildtool.util.xml.XmlUtil;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Bruno Salmon
 */
public final class GwtModuleFile extends XmlModuleFile {

    public GwtModuleFile(ProjectModule module) {
        super(module, false);
    }

    @Override
    Path getModulePath() {
        return resolveFromModuleHomeDirectory("src/main/module.gwt.xml");
    }

    @Override
    Document createInitialDocument() {
        return XmlUtil.parseXmlString("<module>\n" +
                    "\n" +
                    "    <!-- Configuring logging (must be in first position) -->\n" +
                    "    <inherits name=\"com.google.gwt.logging.Logging\"/>\n" +
                    "    <set-property name=\"gwt.logging.enabled\" value=\"TRUE\"/>\n" +
                    "    <set-property name=\"gwt.logging.logLevel\" value=\"FINEST\"/>\n" +
                    "    <set-property name=\"gwt.logging.consoleHandler\" value=\"ENABLED\"/>\n" +
                    "\n" +
                    "    <!-- Listing transitive dependencies modules -->\n" +
                    "\n" +
                    "    <source path=\"mongoose/client/services/bus/conf/gwt\"/>\n" +
                    "    <source path=\"mongoose/shared/domainmodel/gwt\"/>\n" +
                    "    <source path=\"mongoose/client/icons/gwt\"/>\n" +
                    "\n" +
                    "    <!-- Speeding up GWT compilation time in development mode -->\n" +
                    "    <set-property name=\"user.agent\" value=\"safari\"/> <!-- to make only 1 permutation for testing with Chrome -->\n" +
                    "    <set-property name=\"jre.checks.checkLevel\" value=\"MINIMAL\"/>\n" +
                    "\n" +
                    "    <!-- Specify the app entry point class. -->\n" +
                    "    <entry-point class=\"webfx.platform.gwt.services.appcontainer.spi.impl.GwtApplicationContainerProvider\"/>\n" +
                    "\n" +
                    "    <super-source path=\"super\"/>\n" +
                    "    <super-source path=\"\" includes=\"java/io/\"/>\n" +
                    "    <super-source path=\"\" includes=\"java/lang/\"/>\n" +
                    "    <super-source path=\"\" includes=\"java/security/\"/>\n" +
                    "    <super-source path=\"\" includes=\"java/text/\"/>\n" +
                    "    <super-source path=\"\" includes=\"java/util/\"/>\n" +
                    "\n" +
                    "    <public path=\"public\"/>\n" +
                    "\n" +
                    "</module>");
    }

    @Override
    void updateDocument(Document document) {
        document.getDocumentElement().setAttribute("rename-to", getModule().getName().replaceAll("-", "_"));
        Node moduleSourceCommentNode = lookupNode("/module//comment()[2]");
        Node moduleSourceEndNode = moduleSourceCommentNode.getNextSibling();
        getModule().getTransitiveDependencies()
                .stream().collect(Collectors.groupingBy(ModuleDependency::getDestinationModule)).entrySet()
                .stream().sorted(Map.Entry.comparingByKey())
                .forEach(moduleGroup -> {
                    Module module = moduleGroup.getKey();
                    // Ignoring emulated modules because 1) they are destined to the super source, not the source (so they don't need to be listed here) and 2) these modules have been shaded so the original source packages would start with emul (which would be incorrect) if they were listed here
                    if (module.getName().startsWith("webfx-platform-gwt-emul-"))
                        return;
                    Node parentNode = moduleSourceEndNode.getParentNode();
                    String newIndentedLine = "\n    ";
                    Consumer<Node> nodeAppender = node -> {
                        parentNode.insertBefore(document.createTextNode(newIndentedLine), moduleSourceEndNode);
                        parentNode.insertBefore(node, moduleSourceEndNode);
                    };
                    nodeAppender.accept(document.createComment(createModuleSectionLine(module.getName())));
                    moduleGroup.getValue()
                            .stream().sorted(Comparator.comparing(ModuleDependency::getSourceModule)) // Sorting by source module name instead of default (destination module name)
                            .forEach(dep -> nodeAppender.accept(document.createComment(" used by " + dep.getSourceModule() + " (" + dep.getType() + ") ")));
                    String gwtModuleName = getGwtModuleName(module);
                    if (gwtModuleName != null) {
                        Element inherits = document.createElement("inherits");
                        inherits.setAttribute("name", gwtModuleName);
                        nodeAppender.accept(inherits);
                    }
                    if (module instanceof ProjectModule) {
                        ProjectModule projectModule = (ProjectModule) module;
                        projectModule.getDeclaredJavaPackages()
                                .stream().sorted()
                                .forEach(p -> {
                                    Element sourceElement = document.createElement("source");
                                    sourceElement.setAttribute("path", p.replaceAll("\\.", "/"));
                                    nodeAppender.accept(sourceElement);
                                });
                        projectModule.getResourcePackages()
                                .stream().sorted()
                                .forEach(p -> {
                                    Element resourceElement = document.createElement("resource");
                                    resourceElement.setAttribute("path", p.replaceAll("\\.", "/"));
                                    nodeAppender.accept(resourceElement);
                                    Element publicElement = document.createElement("public");
                                    publicElement.setAttribute("path", "");
                                    publicElement.setAttribute("includes", p.replaceAll("\\.", "/") + "/");
                                    nodeAppender.accept(publicElement);
                                });
                        /* Declaring the system properties to ask GWT to replace the System.getProperty() calls with the values at compile time
                           Note: these properties are set with the -setProperty GWT compiler argument when calling the GWT plugin in the root pom.xml */
                        projectModule.getSystemProperties()
                                .stream().sorted()
                                .forEach(p -> {
                                    Element propertyElement = document.createElement("define-configuration-property");
                                    propertyElement.setAttribute("name", p);
                                    propertyElement.setAttribute("is_multi_valued", "false");
                                    nodeAppender.accept(propertyElement);
                                });
                    }
                });
    }

    private static String createModuleSectionLine(String title) {
        int lineLength = Math.max(title.length(), 80);
        int left = (lineLength - title.length()) / 2;
        return Stream.generate(() -> "=").limit(left).collect(Collectors.joining())
                + "< " + title + " >" +
                Stream.generate(() -> "=").limit(lineLength - left - title.length()).collect(Collectors.joining());
    }

    private static String getGwtModuleName(Module module) {
        switch (module.getName()) {
            case "gwt-user": return "com.google.gwt.user.User";
            case "java-logging": return "com.google.gwt.logging.Logging";
            case "elemental2-core": return "elemental2.core.Core";
            case "elemental2-dom": return "elemental2.dom.Dom";
            case "elemental2-svg": return "elemental2.svg.Svg";
            case "gwt-charts": return "com.googlecode.gwt.charts.Charts";
            case "charba": return "org.pepstock.charba.Charba";
            default: return null;
        }
    }
}
