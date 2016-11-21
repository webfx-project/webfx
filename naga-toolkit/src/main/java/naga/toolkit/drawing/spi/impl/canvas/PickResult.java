package naga.toolkit.drawing.spi.impl.canvas;

import naga.toolkit.drawing.shapes.Node;
import naga.toolkit.drawing.shapes.Point2D;
import naga.toolkit.drawing.spi.view.NodeView;

/**
 * @author Bruno Salmon
 */
public class PickResult {

    private final Node node;
    private final NodeView nodeView;
    private final Point2D nodeLocalPoint; // expressed in the node coordinates space - ie without transformation

    PickResult(Node node, NodeView nodeView, Point2D nodeLocalPoint) {
        this.node = node;
        this.nodeView = nodeView;
        this.nodeLocalPoint = nodeLocalPoint;
    }

    public Node getNode() {
        return node;
    }

    public NodeView getNodeView() {
        return nodeView;
    }

    public Point2D getNodeLocalPoint() {
        return nodeLocalPoint;
    }
}
