package naga.toolkit.fx.spi.impl.canvas;

import javafx.beans.value.ObservableValue;
import naga.toolkit.fx.geom.Point2D;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.Parent;
import naga.toolkit.fx.scene.transform.Transform;
import naga.toolkit.fx.spi.DrawingNode;
import naga.toolkit.fx.spi.impl.DrawingImpl;
import naga.toolkit.fx.spi.viewer.NodeViewerFactory;

import java.util.Collection;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public abstract class CanvasDrawingImpl
        <NV extends CanvasNodeViewer<?, CC>, CC>
        extends DrawingImpl {

    public CanvasDrawingImpl(DrawingNode drawingNode, NodeViewerFactory nodeViewerFactory) {
        super(drawingNode, nodeViewerFactory);
    }

    @Override
    protected void updateParentAndChildrenViewers(Parent parent) {
        super.updateParentAndChildrenViewers(parent);
        requestCanvasRepaint();
    }

    @Override
    protected boolean updateViewProperty(Node node, ObservableValue changedProperty) {
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
        for (Node node : nodes)
            paintNode(node, canvasContext);
    }

    public void paintNode(Node node, CC canvasContext) {
        if (node.isVisible()) {
            NV nodeView = (NV) getOrCreateAndBindNodeViewer(node);
            CC nodeCanvasContext = createCanvasContext(canvasContext);
            paintNodeView(nodeView, nodeCanvasContext);
            if (node instanceof Parent)
                paintNodes(((Parent) node).getChildren(), nodeCanvasContext);
            disposeCanvasContext(nodeCanvasContext);
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
        NV nodeView = (NV) getOrCreateAndBindNodeViewer(node);
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

    protected abstract CC createCanvasContext(CC canvasContext);

    protected abstract void disposeCanvasContext(CC canvasContext);

}
