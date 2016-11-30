package naga.toolkit.drawing.spi.impl.canvas;

import javafx.beans.property.Property;
import naga.toolkit.drawing.geom.Point2D;
import naga.toolkit.drawing.scene.Node;
import naga.toolkit.drawing.scene.Parent;
import naga.toolkit.drawing.spi.DrawingNode;
import naga.toolkit.drawing.spi.impl.DrawingImpl;
import naga.toolkit.drawing.spi.view.NodeViewFactory;
import naga.toolkit.transform.Transform;

import java.util.Collection;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public abstract class CanvasDrawingImpl
        <NV extends CanvasNodeView<?, CC>, CC, GS>
        extends DrawingImpl {

    public CanvasDrawingImpl(DrawingNode drawingNode, NodeViewFactory nodeViewFactory) {
        super(drawingNode, nodeViewFactory);
    }

    @Override
    protected void updateParentAndChildrenViews(Parent parent) {
        super.updateParentAndChildrenViews(parent);
        requestCanvasRepaint();
    }

    @Override
    protected boolean updateViewProperty(Node node, Property changedProperty) {
        boolean hitChangedProperty = super.updateViewProperty(node, changedProperty);
        if (hitChangedProperty || changedProperty == null)
            requestCanvasRepaint();
        return hitChangedProperty;
    }

    public void paintCanvas(CC canvasContext) {
        Node rootNode = getRootNode();
        if (rootNode != null)
            paintNode(rootNode, canvasContext);
    }

    private void paintNodes(Collection<Node> nodes, CC canvasContext) {
        GS parentTransform = captureGraphicState(canvasContext);
        for (Node node : nodes) {
            paintNode(node, canvasContext);
            restoreGraphicState(parentTransform, canvasContext);
        }
    }

    private void paintNode(Node node, CC canvasContext) {
        if (node.isVisible()) {
            NV nodeView = (NV) getOrCreateAndBindNodeView(node);
            paintNodeView(nodeView, canvasContext);
            if (node instanceof Parent)
                paintNodes(((Parent) node).getChildren(), canvasContext);
        }
    }

    private void paintNodeView(NV nodeView, CC canvasContext) {
        nodeView.prepareCanvasContext(canvasContext);
        nodeView.paint(canvasContext);
    }

    public PickResult pickNode(Point2D point) {
        Node rootNode = getRootNode();
        return rootNode == null ? null : pickFromNode(point, rootNode);
    }

    private PickResult pickFromNodes(Point2D point, List<Node> nodes) {
        // Looping in inverse order because last nodes are painted above of previous ones so they are priorities for picking
        for (int i = nodes.size() - 1; i >=0; i--) {
            PickResult pickResult = pickFromNode(point, nodes.get(i));
            if (pickResult != null)
                return pickResult;
        }
        return null;
    }

    private PickResult pickFromNode(Point2D point, Node node) {
        if (!node.isVisible())
            return null;
        // The passed point is actually expressed in the parent coordinates space (after the transforms have been applied).
        // Before going further, we need to express it in the node local coordinates space (by applying inverse transforms).
        for (Transform transform : node.localToParentTransforms())
            point = transform.inverseTransform(point);
        // If the node is a parent, we return the pick result from its children if any
        if (node instanceof Parent) {
            PickResult pickResult = pickFromNodes(point, ((Parent) node).getChildren());
            if (pickResult != null)
                return pickResult;
        }
        // Otherwise we ask its view if it contains the point and return this node if this is the case
        NV nodeView = (NV) getOrCreateAndBindNodeView(node);
        return nodeView.containsPoint(point) ? new PickResult(node, nodeView, point) : null;
    }

    private boolean pulseRunning;
    @Override
    public boolean isPulseRunning() {
        return pulseRunning;
    }

    @Override
    protected void startPulse() {
        pulseRunning = true;
    }

    @Override
    public void pulse() {
        super.pulse();
    }

    @Override
    protected void stopPulse() {
        pulseRunning = false;
    }

    public abstract void requestCanvasRepaint();

    protected abstract GS captureGraphicState(CC canvasContext);

    protected abstract void restoreGraphicState(GS canvasState, CC canvasContext);

}
