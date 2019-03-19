package webfx.tool.buildtool.modulefiles;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import webfx.tool.buildtool.Module;
import webfx.tool.buildtool.ProjectModule;

import java.nio.file.Path;
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
        return parseXmlString("<module>\n" +
                    "\n" +
                    "    <!-- Configuring logging (must be in first position) -->\n" +
                    "    <inherits name=\"com.google.gwt.logging.Logging\"/>\n" +
                    "    <set-property name=\"gwt.logging.enabled\" value=\"TRUE\"/>\n" +
                    "    <set-property name=\"gwt.logging.logLevel\" value=\"FINEST\"/>\n" +
                    "    <set-property name=\"gwt.logging.consoleHandler\" value=\"ENABLED\"/>\n" +
                    "\n" +
                    "    <inherits name=\"com.google.gwt.user.User\"/>\n" +
                    "    <inherits name=\"com.googlecode.gwt.charts.Charts\"/>\n" +
                    "    <inherits name=\"elemental2.core.Core\"/>\n" +
                    "    <inherits name=\"elemental2.dom.Dom\"/>\n" +
                    "\n" +
                    "    <!-- Listing modules sources -->\n" +
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
                    "    <entry-point class=\"webfx.platform.shared.services.appcontainer.spi.impl.gwt.GwtApplicationContainerProvider\"/>\n" +
                    "\n" +
                    "    <super-source path=\"super\"/>\n" +
                    "    <super-source path=\"\" includes=\"java/io/\"/>\n" +
                    "    <super-source path=\"\" includes=\"java/lang/\"/>\n" +
                    "    <super-source path=\"\" includes=\"java/nio/\"/>\n" +
                    "    <super-source path=\"\" includes=\"java/security/\"/>\n" +
                    "    <super-source path=\"\" includes=\"java/text/\"/>\n" +
                    "    <super-source path=\"\" includes=\"java/util/\"/>\n" +
                    "    <super-source path=\"\" includes=\"java/time/\"/>\n" +
                    "\n" +
                    "    <public path=\"public\"/>\n" +
                    "\n" +
                    "</module>");
    }

    @Override
    void updateDocument(Document document) {
        document.getDocumentElement().setAttribute("rename-to", getModule().getArtifactId().replaceAll("-", "_"));
        Node moduleSourceCommentNode = lookupNode("/module//comment()[2]");
        Node moduleSourceEndNode = moduleSourceCommentNode.getNextSibling();
        ProjectModule.filterProjectModules(getModule().getThisAndTransitiveDependencies())
                .stream().sorted()
                .forEach(module -> {
                    Node parentNode = moduleSourceEndNode.getParentNode();
                    String newIndentedLine = "\n    ";
                    parentNode.insertBefore(document.createTextNode(newIndentedLine), moduleSourceEndNode);
                    parentNode.insertBefore(document.createComment(createModuleSectionLine(module)), moduleSourceEndNode);
                    module.getDeclaredJavaPackages()
                            .stream().sorted()
                            .forEach(p -> {
                                Element sourceElement = document.createElement("source");
                                sourceElement.setAttribute("path", p.replaceAll("\\.", "/"));
                                parentNode.insertBefore(document.createTextNode(newIndentedLine), moduleSourceEndNode);
                                parentNode.insertBefore(sourceElement, moduleSourceEndNode);
                            });
                    module.getResourcePackages()
                            .stream().sorted()
                            .forEach(p -> {
                                Element resourceElement = document.createElement("resource");
                                resourceElement.setAttribute("path", p.replaceAll("\\.", "/"));
                                parentNode.insertBefore(document.createTextNode(newIndentedLine), moduleSourceEndNode);
                                parentNode.insertBefore(resourceElement, moduleSourceEndNode);
                                Element publicElement = document.createElement("public");
                                publicElement.setAttribute("path", "");
                                publicElement.setAttribute("includes", p.replaceAll("\\.", "/") + "/");
                                parentNode.insertBefore(document.createTextNode(newIndentedLine), moduleSourceEndNode);
                                parentNode.insertBefore(publicElement, moduleSourceEndNode);
                            });
                    /* Declaring the system properties to ask GWT to replace the System.getProperty() calls with the values at compile time
                       Note: these properties are set with the -setProperty GWT compiler argument when calling the GWT plugin in the root pom.xml */
                    module.getSystemProperties()
                            .stream().sorted()
                            .forEach(p -> {
                                Element propertyElement = document.createElement("define-configuration-property");
                                propertyElement.setAttribute("name", p);
                                propertyElement.setAttribute("is_multi_valued", "false");
                                parentNode.insertBefore(document.createTextNode(newIndentedLine), moduleSourceEndNode);
                                parentNode.insertBefore(propertyElement, moduleSourceEndNode);
                            });
                });
    }

    private static String createModuleSectionLine(Module module) {
        String artifactId = module.getArtifactId();
        int lineLength = Math.max(artifactId.length(), 80);
        int left = (lineLength - artifactId.length()) / 2;
        return Stream.generate(() -> "=").limit(left).collect(Collectors.joining())
                + "< " + artifactId + " >" +
                Stream.generate(() -> "=").limit(lineLength - left - artifactId.length()).collect(Collectors.joining());
    }
}
