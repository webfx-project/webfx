package naga.fx.scene;

import javafx.beans.value.ObservableValue;
import naga.fx.geom.Point2D;
import naga.fx.scene.transform.Transform;
import naga.fx.spi.viewer.CanvasNodeViewer;
import naga.fx.spi.viewer.NodeViewerFactory;

import java.util.Collection;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public abstract class CanvasScene
        <NB extends CanvasNodeViewer<?, CC>, CC>

        extends Scene {

    public CanvasScene(NodeViewerFactory nodeViewerFactory) {
        super(nodeViewerFactory);
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
        Parent root = getRoot();
        if (root != null)
            paintNode(root, canvasContext);
    }

    private void paintNodes(Collection<Node> nodes, CC canvasContext) {
        for (Node node : nodes)
            paintNode(node, canvasContext);
    }

    public void paintNode(Node node, CC canvasContext) {
        if (node.isVisible()) {
            NB nodeView = (NB) getOrCreateAndBindNodeViewer(node);
            CC nodeCanvasContext = createCanvasContext(canvasContext);
            paintNodeView(nodeView, nodeCanvasContext);
            if (node instanceof Parent)
                paintNodes(((Parent) node).getChildren(), nodeCanvasContext);
            disposeCanvasContext(nodeCanvasContext);
        }
    }

    private void paintNodeView(NB nodeView, CC canvasContext) {
        nodeView.prepareCanvasContext(canvasContext);
        nodeView.paint(canvasContext);
    }

    public PickResult pickNode(Point2D point) {
        Parent root = getRoot();
        return root == null ? null : pickFromNode(point, root);
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
        NB nodeView = (NB) getOrCreateAndBindNodeViewer(node);
        return nodeView.containsPoint(point) ? new PickResult(node, nodeView, point) : null;
    }

    public abstract void requestCanvasRepaint();

    protected abstract CC createCanvasContext(CC canvasContext);

    protected abstract void disposeCanvasContext(CC canvasContext);

}
