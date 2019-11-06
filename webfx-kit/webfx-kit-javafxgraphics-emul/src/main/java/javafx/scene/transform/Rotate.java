package javafx.scene.transform;

import com.sun.javafx.geom.Point2D;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.GeometryUtil;
import webfx.kit.mapper.peers.javafxgraphics.markers.HasAngleProperty;

/**
 * @author Bruno Salmon
 */
public class Rotate extends PivotTransform implements
        HasAngleProperty {

    /**
     * Creates a default Rotate transform (identity).
     */
    public Rotate() {
    }

    /**
     * Creates a two-dimensional Rotate transform.
     * The pivot point is set to (0,0)
     * @param angle the angle of rotation measured in degrees
     */
    public Rotate(double angle) {
        setAngle(angle);
    }

    /**
     * Creates a two-dimensional Rotate transform with pivot.
     * @param angle the angle of rotation measured in degrees
     * @param pivotX the X coordinate of the rotation pivot point
     * @param pivotY the Y coordinate of the rotation pivot point
     */
    public Rotate(double angle, double pivotX, double pivotY) {
        setAngle(angle);
        setPivotX(pivotX);
        setPivotY(pivotY);
    }

    private final DoubleProperty angleProperty = new SimpleDoubleProperty(0d);
    @Override
    public DoubleProperty angleProperty() {
        return angleProperty;
    }

    @Override
    public Point2D transform(double x, double y) {
        return GeometryUtil.rotate(getPivotX(), getPivotY(), x, y, getAngle());
    }

    @Override
    public Transform createInverse() {
        return new Rotate(-getAngle(), getPivotX(), getPivotY());
    }

    @Override
    public Property[] propertiesInvalidatingCache() {
        return new Property[]{angleProperty, pivotXProperty, pivotYProperty};
    }

    @Override
    public Affine toAffine() {
        double rads = Math.toRadians(getAngle());
        double px = getPivotX();
        double py = getPivotY();
        double sin = Math.sin(rads);
        double cos = Math.cos(rads);
        double mxx = cos;
        double mxy = -sin;
        double tx = px * (1 - cos) + py * sin;
        double myx = sin;
        double myy = cos;
        double ty = py * (1 - cos) - px * sin;
        return new Affine(mxx, mxy, myx, myy, tx, ty);
    }
}
