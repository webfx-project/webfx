package webfx.tool.buildtool.modulefiles;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import webfx.tool.buildtool.ProjectModule;
import webfx.tool.buildtool.util.reusablestream.ReusableStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.IOException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
abstract class XmlModuleFile extends ModuleFile {

    private Document document;

    XmlModuleFile(ProjectModule module) {
        super(module);
    }

    void createDocument() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            document = dBuilder.newDocument();
            // root element
            Element rootElement = document.createElement("module");
            document.appendChild(rootElement);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Document getDocument() {
        if (document == null)
            readFile();
        if (document == null)
            createDocument();
        return document;
    }

    @Override
    public void readFile() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            try {
                document = dBuilder.parse(getModuleFile());
            } catch (IOException e) {
                document = dBuilder.newDocument();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeFile() {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
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
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();
        try {
            XPathExpression expression = xpath.compile(xpathExpression);
            return (NodeList) expression.evaluate(getDocument(), XPathConstants.NODESET);
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

    ReusableStream<String> lookupTextContent(String xPathExpression) {
        return nodeListToReusableStream(lookupNodeList(xPathExpression), Node::getTextContent);
    }

    ReusableStream<webfx.tool.buildtool.Module> lookupModules(String xPathExpression) {
        return lookupTextContent(xPathExpression).map(moduleName -> getModule().getRootModule().findModule(moduleName));
    }
}
