package naga.fx.scene;

import naga.fx.sun.geom.Point2D;
import naga.fx.spi.viewer.NodeViewer;

/**
 * @author Bruno Salmon
 */
public class PickResult {

    private final Node node;
    private final NodeViewer nodeViewer;
    private final Point2D nodeLocalPoint; // expressed in the node coordinates space - ie without transformation

    PickResult(Node node, NodeViewer nodeViewer, Point2D nodeLocalPoint) {
        this.node = node;
        this.nodeViewer = nodeViewer;
        this.nodeLocalPoint = nodeLocalPoint;
    }

    public Node getNode() {
        return node;
    }

    public NodeViewer getNodeViewer() {
        return nodeViewer;
    }

    public Point2D getNodeLocalPoint() {
        return nodeLocalPoint;
    }
}
