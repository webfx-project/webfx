package naga.fx.spi.gwt.html;

import elemental2.HTMLButtonElement;
import elemental2.HTMLElement;
import elemental2.HTMLLabelElement;
import naga.commons.util.Numbers;
import naga.commons.util.collection.Collections;
import naga.fx.spi.gwt.html.viewer.HtmlNodeViewer;
import naga.fx.spi.gwt.shared.HtmlSvgNodeViewer;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fxdata.control.HtmlText;
import naga.fx.scene.Node;
import naga.fx.scene.Parent;
import naga.fx.scene.Scene;
import naga.fx.spi.viewer.NodeViewer;
import naga.fx.properties.Properties;

/**
 * @author Bruno Salmon
 */
public class HtmlScene extends Scene {

    private final HTMLElement container = HtmlUtil.createDivElement();

    public HtmlScene() {
        super(HtmlNodeViewerFactory.SINGLETON);
        HtmlUtil.setStyleAttribute(container, "width", "100%");
        Properties.runNowAndOnPropertiesChange(property -> updateContainerWidth(), widthProperty());
        Properties.runNowAndOnPropertiesChange(property -> updateContainerHeight(), heightProperty());
    }

    public elemental2.Node getSceneNode() {
        return container;
    }

    private void updateContainerWidth() {
        double width = Numbers.doubleValue(getWidth());
        HtmlUtil.setStyleAttribute(container, "width",
                (width > 0 ?
                        width :
                        getRoot() != null ?
                                getRoot().prefWidth(-1) :
                                0)
                        + "px");
    }

    private void updateContainerHeight() {
        double height = Numbers.doubleValue(getHeight());
        HtmlUtil.setStyleAttribute(container, "height",
                (height > 0 ?
                        height :
                        getRoot() != null ?
                                getRoot().prefHeight(-1) :
                                0)
                        + "px");
    }

    @Override
    protected void createAndBindRootNodeViewerAndChildren(Node rootNode) {
        super.createAndBindRootNodeViewerAndChildren(rootNode);
        HtmlUtil.setChildren(container, HtmlSvgNodeViewer.toElement(rootNode, this));
    }

    @Override
    protected void updateParentAndChildrenViewers(Parent parent) {
        if (!(parent instanceof HtmlText)) {
            elemental2.Node parentNode = HtmlSvgNodeViewer.toElement(parent, this);
            HtmlUtil.setChildren(parentNode, Collections.convert(parent.getChildren(), node -> HtmlSvgNodeViewer.toElement(node, this)));
        }
    }

    @Override
    protected NodeViewer<Node> createNodeViewer(Node node) {
        NodeViewer<Node> nodeViewer = super.createNodeViewer(node);
        if (nodeViewer instanceof HtmlNodeViewer) {
            HtmlNodeViewer htmlNodeView = (HtmlNodeViewer) nodeViewer;
            HTMLElement htmlElement = (HTMLElement) htmlNodeView.getElement();
            HtmlUtil.absolutePosition(htmlElement);
            if (htmlElement instanceof HTMLButtonElement || htmlElement instanceof HTMLLabelElement || htmlElement.tagName.equals("SPAN") && !(node instanceof HtmlText))
                htmlElement.style.whiteSpace = "nowrap";
        }
        return nodeViewer;
    }

}
