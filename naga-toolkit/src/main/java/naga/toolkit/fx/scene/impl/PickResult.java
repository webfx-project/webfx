package naga.toolkit.fx.scene.impl;

import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.geom.Point2D;
import naga.toolkit.fx.spi.viewer.NodeViewer;

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
