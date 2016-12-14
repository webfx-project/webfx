package naga.providers.toolkit.html.fx.html;

import elemental2.HTMLButtonElement;
import elemental2.HTMLElement;
import elemental2.HTMLLabelElement;
import naga.commons.util.collection.Collections;
import naga.providers.toolkit.html.fx.html.viewer.HtmlNodeViewer;
import naga.providers.toolkit.html.fx.shared.HtmlSvgNodeViewer;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.fx.ext.control.HtmlText;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.Parent;
import naga.toolkit.fx.spi.impl.DrawingImpl;
import naga.toolkit.fx.spi.viewer.NodeViewer;

/**
 * @author Bruno Salmon
 */
public class HtmlDrawing extends DrawingImpl {

    HtmlDrawing(HtmlDrawingNode htmlDrawingNode) {
        super(htmlDrawingNode, HtmlNodeViewerFactory.SINGLETON);
    }

    @Override
    protected void createAndBindRootNodeViewerAndChildren(Node rootNode) {
        super.createAndBindRootNodeViewerAndChildren(rootNode);
        elemental2.Node parent = drawingNode.unwrapToNativeNode();
        HtmlUtil.setChildren(parent, HtmlSvgNodeViewer.toElement(rootNode, this));
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
