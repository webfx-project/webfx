package webfx.tool.buildtool.modulefiles;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import webfx.tool.buildtool.ModuleDependency;
import webfx.tool.buildtool.ProjectModule;
import webfx.tool.buildtool.Target;
import webfx.tool.buildtool.TargetTag;
import webfx.tool.buildtool.util.reusablestream.ReusableStream;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;

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
        document = parseXmlFile(getModuleFile());
    }

    Document parseXmlSource(InputSource is) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            try {
                return dBuilder.parse(is);
            } catch (IOException e) {
                return dBuilder.newDocument();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    Document parseXmlFile(File xmlFile) {
        return parseXmlSource(new InputSource(xmlFile.toURI().toASCIIString()));
    }

    Document parseXmlString(String xmlString) {
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xmlString));
        return parseXmlSource(is);
    }

    public void updateAndWrite() {
        updateDocument(getDocument());
        writeFile();
    }

    @Override
    public void writeFile() {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            //transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty("http://www.oracle.com/xml/is-standalone", "yes"); // This is to fix the missing line feed after xml declaration
            Document document = getDocument();
            document.getDocumentElement().normalize();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(getModuleFile());
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    NodeList lookupNodeList(String xpathExpression) {
        return lookup(xpathExpression, XPathConstants.NODESET);
    }

    Node lookupNode(String xpathExpression) {
        return lookup(xpathExpression, XPathConstants.NODE);
    }

    private <T> T lookup(String xpathExpression, QName returnType) {
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();
        try {
            XPathExpression expression = xpath.compile(xpathExpression);
            return (T) expression.evaluate(getDocument(), returnType);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    <T> ReusableStream<T> nodeListToReusableStream(NodeList nodeList, Function<Node, ? extends T> transformer) {
        return ReusableStream.create(() -> new Spliterators.AbstractSpliterator<T>(nodeList.getLength(), Spliterator.SIZED) {
            private int index = 0;
            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                if (index >= nodeList.getLength())
                    return false;
                Node moduleNode = nodeList.item(index++);
                action.accept(transformer.apply(moduleNode));
                return true;
            }
        });
    }

    ReusableStream<String> lookupNodeListTextContent(String xPathExpression) {
        return nodeListToReusableStream(lookupNodeList(xPathExpression), Node::getTextContent);
    }

    ReusableStream<String> lookupNodeListAttribute(String xPathExpression, String attribute) {
        return nodeListToReusableStream(lookupNodeList(xPathExpression), node -> getAttributeValue(node, attribute));
    }

    ReusableStream<ModuleDependency> lookupDependencies(String xPathExpression, ModuleDependency.Type type) {
        return nodeListToReusableStream(lookupNodeList(xPathExpression), node ->
                new ModuleDependency(
                        getModule(),
                        getModule().getRootModule().findModule(node.getTextContent()),
                        type,
                        getBooleanAttributeValue(node, "optional"),
                        getAttributeValue(node, "scope"),
                        getAttributeValue(node, "classifier"),
                        getTargetAttributeValue(node, "executable-target")
                ));
    }

    private static String getAttributeValue(Node node, String name) {
        if (node == null)
            return null;
        Node namedItem = node.getAttributes().getNamedItem(name);
        return namedItem == null ? null : namedItem.getNodeValue();
    }

    static boolean getBooleanAttributeValue(Node node, String name) {
        return "true".equalsIgnoreCase(getAttributeValue(node, name));
    }

    private Target getTargetAttributeValue(Node node, String name) {
        String stringValue = getAttributeValue(node, name);
        return stringValue == null ? null : new Target(TargetTag.parseTags(stringValue));
    }
}
