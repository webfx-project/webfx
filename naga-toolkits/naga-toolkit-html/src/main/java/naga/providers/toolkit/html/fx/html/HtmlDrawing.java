package naga.providers.toolkit.html.fx.html;

import elemental2.*;
import naga.commons.util.collection.Collections;
import naga.providers.toolkit.html.fx.html.view.HtmlNodeView;
import naga.providers.toolkit.html.fx.shared.HtmlSvgNodeView;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.Parent;
import naga.toolkit.fx.spi.impl.DrawingImpl;
import naga.toolkit.fx.spi.view.NodeView;

/**
 * @author Bruno Salmon
 */
public class HtmlDrawing extends DrawingImpl {

    HtmlDrawing(HtmlDrawingNode htmlDrawingNode) {
        super(htmlDrawingNode, HtmlNodeViewFactory.SINGLETON);
    }

    @Override
    protected void createAndBindRootNodeViewAndChildren(Node rootNode) {
        super.createAndBindRootNodeViewAndChildren(rootNode);
        elemental2.Node parent = drawingNode.unwrapToNativeNode();
        HtmlUtil.setChildren(parent, HtmlSvgNodeView.toElement(rootNode, this));
    }

    @Override
    protected void updateParentAndChildrenViews(Parent parent) {
        elemental2.Node parentNode = HtmlSvgNodeView.toElement(parent, this);
        HtmlUtil.setChildren(parentNode, Collections.convert(parent.getChildren(), node -> HtmlSvgNodeView.toElement(node, this)));
    }

    @Override
    protected NodeView<Node> createNodeView(Node node) {
        NodeView<Node> nodeView = super.createNodeView(node);
        if (nodeView instanceof HtmlNodeView) {
            HtmlNodeView htmlNodeView = (HtmlNodeView) nodeView;
            HTMLElement htmlElement = (HTMLElement) htmlNodeView.getElement();
            HtmlUtil.absolutePosition(htmlElement);
            if (htmlElement instanceof HTMLButtonElement || htmlElement instanceof HTMLLabelElement || htmlElement.tagName.equals("SPAN"))
                htmlElement.style.whiteSpace = "nowrap";
        }
        return nodeView;
    }

}
