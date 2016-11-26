package naga.providers.toolkit.html.drawing.html;

import elemental2.Element;
import naga.commons.util.collection.Collections;
import naga.providers.toolkit.html.drawing.html.view.HtmlNodeView;
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
        HtmlUtil.setChildren(parent, getNodeElement(rootNode));
    }

    @Override
    protected void updateParentAndChildrenViews(Parent parent) {
        elemental2.Node svgParent = getNodeElement(parent);
        HtmlUtil.setChildren(svgParent, Collections.convert(parent.getChildren(), this::getNodeElement));
    }

    private HtmlNodeView getOrCreateAndBindAndCastNodeView(Node node) {
        NodeView nodeView = getOrCreateAndBindNodeView(node); // Should be a FxDrawableView (but may be UnimplementedNodeView if no view factory is registered for this node)
        if (nodeView instanceof HtmlNodeView) // Should be a HtmlNodeView
            return (HtmlNodeView) nodeView;
        // Shouldn't happen unless no view factory is registered for this node (probably UnimplementedNodeView was returned)
        return null; // returning null in this case to indicate there is no view to show
    }

    private Element getNodeElement(Node node) {
        HtmlNodeView nodeView = getOrCreateAndBindAndCastNodeView(node);
        return nodeView == null ? null : nodeView.getElement();
    }

}
