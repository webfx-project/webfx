package naga.providers.toolkit.html.drawing.html;

import elemental2.Element;
import elemental2.HTMLButtonElement;
import elemental2.HTMLElement;
import elemental2.HTMLLabelElement;
import naga.commons.util.collection.Collections;
import naga.providers.toolkit.html.drawing.html.view.HtmlNodeView;
import naga.providers.toolkit.html.drawing.svg.view.SvgNodeView;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.drawing.scene.Node;
import naga.toolkit.drawing.scene.Parent;
import naga.toolkit.drawing.spi.impl.DrawingImpl;
import naga.toolkit.drawing.spi.view.NodeView;

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
        HtmlUtil.setChildren(parent, getNodeElementForParent(rootNode));
    }

    @Override
    protected void updateParentAndChildrenViews(Parent parent) {
        elemental2.Node parentNode = getNodeElementForParent(parent);
        HtmlUtil.setChildren(parentNode, Collections.convert(parent.getChildren(), this::getNodeElementForParent));
    }

    private Element getNodeElementForParent(Node node) {
        NodeView nodeView = getOrCreateAndBindNodeView(node); // Should be a SvgNodeView or a HtmlNodeView
        if (nodeView instanceof SvgNodeView) // SvgNodeView case
            return ((SvgNodeView) nodeView).getElement();
        if (nodeView instanceof HtmlNodeView) // HtmlNodeView case
            return ((HtmlNodeView) nodeView).getContainer();
        // Shouldn't happen unless no view factory is registered for this node (probably UnimplementedNodeView was returned)
        return null; // returning null in this case to indicate there is no view to show
    }

    @Override
    protected NodeView<Node> createNodeView(Node node) {
        NodeView<Node> nodeView = super.createNodeView(node);
        if (nodeView instanceof HtmlNodeView) {
            HtmlNodeView htmlNodeView = (HtmlNodeView) nodeView;
            HTMLElement htmlElement = (HTMLElement) htmlNodeView.getElement();
            HtmlUtil.absolutePosition(htmlElement);
            if (htmlElement instanceof HTMLButtonElement || htmlElement instanceof HTMLLabelElement)
                htmlElement.style.whiteSpace = "nowrap";
        }
        return nodeView;
    }

}
