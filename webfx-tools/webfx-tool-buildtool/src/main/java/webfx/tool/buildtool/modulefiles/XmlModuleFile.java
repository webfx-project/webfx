package webfx.tool.buildtool.modulefiles;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import webfx.tool.buildtool.ProjectModule;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
            document = dBuilder.parse(getModuleFile());
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
}
