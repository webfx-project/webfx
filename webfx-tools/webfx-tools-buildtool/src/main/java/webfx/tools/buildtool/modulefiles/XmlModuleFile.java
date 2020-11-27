package webfx.tools.buildtool.modulefiles;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import webfx.tools.buildtool.ModuleDependency;
import webfx.tools.buildtool.ProjectModule;
import webfx.tools.buildtool.Target;
import webfx.tools.buildtool.TargetTag;
import webfx.tools.buildtool.util.textfile.TextFileReaderWriter;
import webfx.tools.util.reusablestream.ReusableStream;
import webfx.tools.buildtool.util.xml.XmlUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * @author Bruno Salmon
 */
abstract class XmlModuleFile extends ModuleFile {

    private Document document;
    private final boolean readFileIfExists;

    XmlModuleFile(ProjectModule module, boolean readFileIfExists) {
        super(module);
        this.readFileIfExists = readFileIfExists;
    }

    Document getDocument() {
        if (document == null & readFileIfExists)
            readFile();
        if (document == null)
            createDocument();
        return document;
    }

    void createDocument() {
        document = createInitialDocument();
        updateDocument(document);
    }

    Document createInitialDocument() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            return dBuilder.newDocument();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    void updateDocument(Document document) {
        clearDocument(document);
        document.appendChild(document.createElement("module"));
    }

    void clearDocument(Document document) {
        clearNodeChildren(document);
    }

    void clearNodeChildren(Node node) {
        Node firstChild;
        while ((firstChild = node.getFirstChild()) != null)
            node.removeChild(firstChild);
    }

    @Override
    public void readFile() {
        document = XmlUtil.parseXmlFile(getModuleFile());
    }

    public void updateAndWrite() {
        updateDocument(getDocument());
        writeFile();
    }

    @Override
    public void writeFile() {
        TextFileReaderWriter.writeTextFileIfNewOrModified(XmlUtil.formatXmlText(getDocument()), getModulePath());
    }

    NodeList lookupNodeList(String xpathExpression) {
        return XmlUtil.lookupNodeList(getDocument(), xpathExpression);
    }

    Node lookupNode(String xpathExpression) {
        return XmlUtil.lookupNode(getDocument(), xpathExpression);
    }

    String lookupNodeTextContent(String xpathExpression) {
        Node node = lookupNode(xpathExpression);
        return node == null ? null : node.getTextContent();
    }

    ReusableStream<String> lookupNodeListTextContent(String xPathExpression) {
        return XmlUtil.nodeListToReusableStream(lookupNodeList(xPathExpression), Node::getTextContent);
    }

    ReusableStream<String> lookupNodeListAttribute(String xPathExpression, String attribute) {
        return XmlUtil.nodeListToReusableStream(lookupNodeList(xPathExpression), node -> XmlUtil.getAttributeValue(node, attribute));
    }

    ReusableStream<ModuleDependency> lookupDependencies(String xPathExpression, ModuleDependency.Type type) {
        return XmlUtil.nodeListToReusableStream(lookupNodeList(xPathExpression), node ->
                new ModuleDependency(
                        getModule(),
                        getModule().getRootModule().findModule(node.getTextContent()),
                        type,
                        XmlUtil.getBooleanAttributeValue(node, "optional"),
                        XmlUtil.getAttributeValue(node, "scope"),
                        XmlUtil.getAttributeValue(node, "classifier"),
                        getTargetAttributeValue(node, "executable-target")
                ));
    }

    private Target getTargetAttributeValue(Node node, String name) {
        String stringValue = XmlUtil.getAttributeValue(node, name);
        return stringValue == null ? null : new Target(TargetTag.parseTags(stringValue));
    }
}
