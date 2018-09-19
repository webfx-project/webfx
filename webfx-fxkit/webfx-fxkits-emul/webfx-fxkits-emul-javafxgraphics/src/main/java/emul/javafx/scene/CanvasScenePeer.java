package emul.javafx.scene;

import emul.com.sun.javafx.geom.Point2D;
import emul.javafx.collections.ListChangeListener;
import emul.javafx.scene.input.PickResult;
import emul.javafx.scene.transform.Transform;
import webfx.fxkits.core.mapper.spi.CanvasNodePeer;
import webfx.fxkits.core.mapper.spi.impl.peer.ScenePeerBase;

import java.util.Collection;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public abstract class CanvasScenePeer
        <NB extends CanvasNodePeer<?, CC>, CC>

        extends ScenePeerBase {

    public CanvasScenePeer(Scene scene) {
        super(scene);
    }

    @Override
    public void updateParentAndChildrenPeers(Parent parent, ListChangeListener.Change<Node> childrenChange) {
        scene.updateChildrenPeers(parent.getChildren());
        requestCanvasRepaint();
    }

    @Override
    public void onPropertyHit() {
        requestCanvasRepaint();
    }

    public void paintCanvas(CC canvasContext) {
        Parent root = scene.getRoot();
        if (root != null)
            paintNode(root, canvasContext);
    }

    private void paintNodes(Collection<Node> nodes, CC canvasContext) {
        for (Node node : nodes)
            paintNode(node, canvasContext);
    }

    public void paintNode(Node node, CC canvasContext) {
        if (node.isVisible()) {
            NB peer = (NB) scene.getOrCreateAndBindNodePeer(node);
            CC nodeCanvasContext = createCanvasContext(canvasContext);
            paintNodeView(peer, nodeCanvasContext);
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
        Parent root = scene.getRoot();
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
        NB nodeView = (NB) scene.getOrCreateAndBindNodePeer(node);
        return nodeView.containsPoint(point) ? new PickResult(node, point.x, point.y) : null;
    }

    public abstract void requestCanvasRepaint();

    protected abstract CC createCanvasContext(CC canvasContext);

    protected abstract void disposeCanvasContext(CC canvasContext);

}
