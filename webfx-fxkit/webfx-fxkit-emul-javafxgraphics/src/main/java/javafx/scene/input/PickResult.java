package javafx.scene.input;

import com.sun.javafx.geom.Point2D;
import javafx.event.EventTarget;
import javafx.scene.Node;

import static com.sun.javafx.scene.input.InputEventUtils.toSunPoint2D;

/**
 * @author Bruno Salmon
 */
public class PickResult {
    private final Node intersectedNode;
    private final Point2D intersectedPoint;

    /**
     * Creates a new instance of PickResult for a non-3d-shape target.
     * Sets face to FACE_UNDEFINED and texCoord to null.
     * @param node The intersected node
     * @param point The intersected point in local coordinate of the picked Node
     */
    public PickResult(Node node, Point2D point) {
        this.intersectedNode = node;
        this.intersectedPoint = point;
    }

    /**
     * Creates a pick result for a 2D case where no additional information is needed.
     * Converts the given scene coordinates to the target's local coordinate space
     * and stores the value as the intersected point. Sets intersected node
     * to the given target, distance to 1.0,
     * face to FACE_UNDEFINED and texCoord to null.
     * @param target The picked target (null in case of a Scene)
     * @param sceneX The scene X coordinate
     * @param sceneY The scene Y coordinate
     */
    public PickResult(EventTarget target, double sceneX, double sceneY) {
        this(target instanceof Node ? (Node) target : null,
                target instanceof Node ? toSunPoint2D(((Node) target).sceneToLocal(sceneX, sceneY)) : new Point2D((float) sceneX, (float) sceneY));
    }

    public Point2D getIntersectedPoint() {
        return intersectedPoint;
    }

    public Node getIntersectedNode() {
        return intersectedNode;
    }
}
