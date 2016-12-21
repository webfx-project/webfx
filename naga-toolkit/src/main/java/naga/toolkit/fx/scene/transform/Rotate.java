package naga.toolkit.fx.scene.transform;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.fx.geometry.GeometryUtil;
import naga.toolkit.fx.geom.Point2D;
import naga.toolkit.fx.properties.markers.HasAngleProperty;
import naga.toolkit.fx.properties.markers.HasPivotXProperty;
import naga.toolkit.fx.properties.markers.HasPivotYProperty;

/**
 * @author Bruno Salmon
 */
public class Rotate extends Transform implements
        HasAngleProperty,
        HasPivotXProperty,
        HasPivotYProperty {

    public Rotate() {
    }

    public Rotate(double angle, double pivotX, double pivotY) {
        setAngle(angle);
        setPivotX(pivotX);
        setPivotY(pivotY);
    }

    private final Property<Double> angleProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> angleProperty() {
        return angleProperty;
    }

    private final Property<Double> pivotXProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> pivotXProperty() {
        return pivotXProperty;
    }

    private final Property<Double> pivotYProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> pivotYProperty() {
        return pivotYProperty;
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
    protected Property[] propertiesInvalidatingCache() {
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
