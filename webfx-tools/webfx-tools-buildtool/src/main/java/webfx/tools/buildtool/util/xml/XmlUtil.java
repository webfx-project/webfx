package webfx.tools.buildtool.util.xml;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import webfx.tools.util.reusablestream.ReusableStream;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author bruno
 */
public final class XmlUtil {

    public static Document parseXmlFile(File xmlFile) {
        return parseXmlSource(new InputSource(xmlFile.toURI().toASCIIString()));
    }

    public static Document parseXmlString(String xmlString) {
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xmlString));
        return parseXmlSource(is);
    }

    public static Document parseXmlSource(InputSource is) {
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

    public static String formatXmlText(Node node) {
        return formatText(node, "xml");
    }

    public static String formatHtmlText(Node node) {
        return formatText(node, "html");
    }

    private static String formatText(Node node, String method) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            boolean isDocument = node instanceof Document;
            if (!isDocument)
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            else
                transformer.setOutputProperty(OutputKeys.STANDALONE, "yes"); // This is to fix the missing line feed after xml declaration
            transformer.setOutputProperty(OutputKeys.METHOD, method);
            //transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            //node.normalize();
            DOMSource source = new DOMSource(node);
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            transformer.transform(source, result);
            return sw.toString();
        } catch (TransformerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static NodeList lookupNodeList(Object item, String xpathExpression) {
        return lookup(item, xpathExpression, XPathConstants.NODESET);
    }

    public static Node lookupNode(Object item, String xpathExpression) {
        return lookup(item, xpathExpression, XPathConstants.NODE);
    }

    private static <T> T lookup(Object item, String xpathExpression, QName returnType) {
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();
        try {
            XPathExpression expression = xpath.compile(xpathExpression);
            return (T) expression.evaluate(item, returnType);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> ReusableStream<T> nodeListToReusableStream(NodeList nodeList, Function<Node, ? extends T> transformer) {
        return ReusableStream.create(() -> new Spliterators.AbstractSpliterator<>(nodeList.getLength(), Spliterator.SIZED) {
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

    public static String getAttributeValue(Node node, String name) {
        NamedNodeMap attributes = node == null ? null : node.getAttributes();
        Node namedItem = attributes == null ? null : attributes.getNamedItem(name);
        return namedItem == null ? null : namedItem.getNodeValue();
    }

    public static boolean getBooleanAttributeValue(Node node, String name) {
        return "true".equalsIgnoreCase(getAttributeValue(node, name));
    }
}
