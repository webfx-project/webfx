package javafx.scene.transform;

import com.sun.javafx.geom.Point2D;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.GeometryUtil;
import webfx.fxkit.mapper.spi.impl.peer.markers.HasAngleProperty;

/**
 * @author Bruno Salmon
 */
public class Rotate extends PivotTransform implements
        HasAngleProperty {

    public Rotate() {
    }

    public Rotate(double angle, double pivotX, double pivotY) {
        super(pivotX, pivotY);
        setAngle(angle);
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
