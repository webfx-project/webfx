package naga.fx.spi.gwt.svg;

import elemental2.Element;
import elemental2.HTMLElement;
import naga.commons.util.Numbers;
import naga.commons.util.collection.Collections;
import naga.fx.spi.gwt.html.viewer.HtmlNodeViewer;
import naga.fx.spi.gwt.shared.HtmlSvgNodeViewer;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.spi.gwt.util.SvgUtil;
import naga.fx.scene.Node;
import naga.fx.scene.Parent;
import naga.fx.scene.Scene;
import naga.fx.spi.viewer.NodeViewer;
import naga.fx.properties.Properties;

/**
 * @author Bruno Salmon
 */
public class SvgScene extends Scene {

    private final Element container = SvgUtil.createSvgElement();
    private final Element defsElement = SvgUtil.createSvgDefs();

    public SvgScene() {
        super(SvgNodeViewerFactory.SINGLETON);
        HtmlUtil.setAttribute(container, "width", "100%");
        Properties.runNowAndOnPropertiesChange(property -> updateContainerWidth(), widthProperty());
        Properties.runNowAndOnPropertiesChange(property -> updateContainerHeight(), heightProperty());
    }

    private void updateContainerWidth() {
        double width = Numbers.doubleValue(getWidth());
        HtmlUtil.setAttribute(container, "width",
                (width > 0 ?
                        width :
                        getRoot() != null ?
                                getRoot().prefWidth(-1) :
                                0)
                        + "px");
    }

    private void updateContainerHeight() {
        double height = Numbers.doubleValue(getHeight());
        HtmlUtil.setAttribute(container, "height",
                (height > 0 ?
                        height :
                        getRoot() != null ?
                                getRoot().prefHeight(-1) :
                                0)
                        + "px");
    }

    public Element addDef(Element def) {
        defsElement.appendChild(def);
        return def;
    }

    @Override
    protected void createAndBindRootNodeViewerAndChildren(Node rootNode) {
        super.createAndBindRootNodeViewerAndChildren(rootNode);
        HtmlUtil.setChildren(container, defsElement, HtmlSvgNodeViewer.toElement(rootNode, this));
    }

    @Override
    protected void updateParentAndChildrenViewers(Parent parent) {
        elemental2.Node svgParent = HtmlSvgNodeViewer.toElement(parent, this);
        HtmlUtil.setChildren(svgParent, Collections.convert(parent.getChildren(), node -> HtmlSvgNodeViewer.toElement(node, this)));
    }

    @Override
    protected NodeViewer<Node> createNodeViewer(Node node) {
        NodeViewer<Node> nodeViewer = super.createNodeViewer(node);
        if (nodeViewer instanceof HtmlNodeViewer) {
            HtmlNodeViewer htmlNodeView = (HtmlNodeViewer) nodeViewer;
            HTMLElement htmlElement = (HTMLElement) htmlNodeView.getElement();
            Element foreignObject = SvgUtil.createSvgElement("foreignObject");
            foreignObject.setAttribute("width", "100%");
            foreignObject.setAttribute("height", "100%");
            HtmlUtil.setChild(foreignObject, htmlElement);
            htmlNodeView.setContainer(foreignObject);
        }
        return nodeViewer;
    }
}
