package javafx.scene.transform;

import com.sun.javafx.geom.Point2D;
import dev.webfx.kit.mapper.peers.javafxgraphics.markers.HasAngleProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.GeometryUtil;
import javafx.geometry.Point3D;

/**
 * @author Bruno Salmon
 */
public class Rotate extends PivotTransform implements
        HasAngleProperty {

    /**
     * Specifies the X-axis as the axis of rotation.
     */
    public static final Point3D X_AXIS = new Point3D(1,0,0);

    /**
     * Specifies the Y-axis as the axis of rotation.
     */
    public static final Point3D Y_AXIS = new Point3D(0,1,0);

    /**
     * Specifies the Z-axis as the axis of rotation.
     */
    public static final Point3D Z_AXIS = new Point3D(0,0,1);


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
     * Creates a three-dimensional Rotate transform.
     * The pivot point is set to (0,0,0)
     * @param angle the angle of rotation measured in degrees
     * @param axis the axis of rotation
     */
    public Rotate(double angle, Point3D axis) {
        setAngle(angle);
        setAxis(axis);
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

    private final DoubleProperty angleProperty = new SimpleDoubleProperty(0d) {
        @Override
        protected void invalidated() {
            transformChanged();
        }
    };
    @Override
    public DoubleProperty angleProperty() {
        return angleProperty;
    }

    /**
     * Defines the axis of rotation at the pivot point.
     */
    private ObjectProperty<Point3D> axis;


    public final void setAxis(Point3D value) {
        axisProperty().set(value);
    }

    public final Point3D getAxis() {
        return axis == null ? Z_AXIS : axis.get();
    }

    public final ObjectProperty<Point3D> axisProperty() {
        if (axis == null) {
            axis = new ObjectPropertyBase<Point3D>(Z_AXIS) {

                @Override
                public void invalidated() {
                    transformChanged();
                }

                @Override
                public Object getBean() {
                    return Rotate.this;
                }

                @Override
                public String getName() {
                    return "axis";
                }
            };
        }
        return axis;
    }

    @Override
    public Point2D transform(double x, double y) {
        if (Z_AXIS.equals(getAxis())) // Ignoring 3D transforms for now
            return GeometryUtil.rotate(getPivotX(), getPivotY(), x, y, getAngle());
        return new Point2D((float) x, (float) y);
    }

    @Override
    public Transform createInverse() {
        return new Rotate(-getAngle(), getPivotX(), getPivotY());
    }

    @Override
    protected Affine createAffine() {
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

    public double getMxx() {
        return toAffine().getMxx();
    }

    public double getMxy() {
        return toAffine().getMxy();
    }

    public double getMyx() {
        return toAffine().getMyx();
    }

    public double getMyy() {
        return toAffine().getMyy();
    }

    public double getTx() {
        return toAffine().getTx();
    }

    public double getTy() {
        return toAffine().getTy();
    }
}
