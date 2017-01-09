package naga.fx.scene.input;

import naga.fx.event.EventTarget;
import naga.fx.geometry.Point2D;
import naga.fx.geometry.Point3D;
import naga.fx.scene.Node;

/**
 * A container object that contains the result of a pick event
 * @since JavaFX 8.0
 */
public class PickResult {

    /**
     * An undefined face. This value is used for the intersected face
     * if the picked node has no user-specified faces.
     */
    public static final int FACE_UNDEFINED = -1;

    private Node node;
    private Point3D point;
    private double distance = Double.POSITIVE_INFINITY;
    private int face = -1;
    private Point3D normal;
    private Point2D texCoord;

    /**
     * Creates a new instance of PickResult.
     * @param node The intersected node
     * @param point The intersected point in local coordinate of the picked Node
     * @param distance The intersected distance between camera position and the picked Node
     * @param face The intersected face of the picked Node
     * @param texCoord The intersected texture coordinates of the picked Node
     */
    public PickResult(Node node, Point3D point, double distance, int face, Point2D texCoord) {
        this.node = node;
        this.point = point;
        this.distance = distance;
        this.face = face;
        this.normal = null;
        this.texCoord = texCoord;
    }

    /**
     * Creates a new instance of PickResult.
     *
     * @param node The intersected node
     * @param point The intersected point in local coordinate of the picked Node
     * @param distance The intersected distance between camera position and the picked Node
     * @param face The intersected face of the picked Node
     * @param texCoord The intersected texture coordinates of the picked Node
     * @param normal The intersected normal of the picked Node
     *
     * @since JavaFX 8u40
     */
    public PickResult(Node node, Point3D point, double distance, int face, Point3D normal, Point2D texCoord) {
        this.node = node;
        this.point = point;
        this.distance = distance;
        this.face = face;
        this.normal = normal;
        this.texCoord = texCoord;
    }

    /**
     * Creates a new instance of PickResult for a non-3d-shape target.
     * Sets face to FACE_UNDEFINED and texCoord to null.
     * @param node The intersected node
     * @param point The intersected point in local coordinate of the picked Node
     * @param distance The intersected distance between camera position and the picked Node
     */
    public PickResult(Node node, Point3D point, double distance) {
        this.node = node;
        this.point = point;
        this.distance = distance;
        this.face = FACE_UNDEFINED;
        this.normal = null;
        this.texCoord = null;
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
        this(target instanceof Node ? (Node) target : null, /* temporary commented until implemented:  target instanceof Node ? ((Node) target).sceneToLocal(sceneX, sceneY, 0) :*/ new Point3D(sceneX, sceneY, 0),
                1.0);
    }


    /**
     * Returns the intersected node.
     * Returns null if there was no intersection with any node and the scene
     * was picked.
     *
     * @return the picked node or null if no node was picked
     */
    public final Node getIntersectedNode() {
        return node;
    }

    /**
     * Returns the intersected point in local coordinate of the picked Node.
     * If no node was picked, it returns the intersected point with the
     * projection plane.
     *
     * @return new Point3D presenting the intersected point
     */
    public final Point3D getIntersectedPoint() {
        return point;
    }

    /**
     * Returns the intersected distance between camera position
     * and the intersected point.
     *
     * @return the distance from camera to the intersection
     */
    public final double getIntersectedDistance() {
        return distance;
    }

    /**
     * Returns the intersected face of the picked Node, FACE_UNDEFINED
     *         if the node doesn't have user-specified faces
     *         or was picked on bounds.
     *
     * @return the picked face
     */
    public final int getIntersectedFace() {
        return face;
    }

    /**
     * Return the intersected normal of the picked 3d shape. If the picked
     * target is not Shape3D or has pickOnBounds==true, it returns null.
     *
     * @return a new Point3D presenting the intersected normal
     *
     * @since JavaFX 8u40
     */
    public final Point3D getIntersectedNormal() {
        return normal;
    }

    /**
     * Return the intersected texture coordinates of the picked 3d shape.
     * If the picked target is not Shape3D or has pickOnBounds==true,
     * it returns null.
     *
     * @return a new Point2D presenting the intersected TexCoord
     */
    public final Point2D getIntersectedTexCoord() {
        return texCoord;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("PickResult [");
        sb.append("node = ").append(getIntersectedNode())
                .append(", point = ").append(getIntersectedPoint())
                .append(", distance = ").append(getIntersectedDistance());
        if (getIntersectedFace() != FACE_UNDEFINED)
            sb.append(", face = ").append(getIntersectedFace());
        if (getIntersectedNormal() != null)
            sb.append(", normal = ").append(getIntersectedNormal());
        if (getIntersectedTexCoord() != null)
            sb.append(", texCoord = ").append(getIntersectedTexCoord());
        return sb.toString();
    }
}
