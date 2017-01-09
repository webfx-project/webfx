package naga.fx.scene;

import naga.fx.spi.peer.NodePeer;
import naga.fx.sun.geom.Point2D;

/**
 * @author Bruno Salmon
 */
public class PickResult {

    private final Node node;
    private final NodePeer nodePeer;
    private final Point2D nodeLocalPoint; // expressed in the node coordinates space - ie without transformation

    PickResult(Node node, NodePeer nodePeer, Point2D nodeLocalPoint) {
        this.node = node;
        this.nodePeer = nodePeer;
        this.nodeLocalPoint = nodeLocalPoint;
    }

    public Node getNode() {
        return node;
    }

    public NodePeer getNodePeer() {
        return nodePeer;
    }

    public Point2D getNodeLocalPoint() {
        return nodeLocalPoint;
    }
}
