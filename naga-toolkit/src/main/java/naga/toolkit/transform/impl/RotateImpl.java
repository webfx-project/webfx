package naga.toolkit.transform.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.drawing.GeometryUtil;
import naga.toolkit.drawing.geom.Point2D;
import naga.toolkit.transform.Rotate;
import naga.toolkit.transform.Transform;

/**
 * @author Bruno Salmon
 */
public class RotateImpl extends TransformImpl implements Rotate {

    public RotateImpl() {
    }

    public RotateImpl(double angle, double pivotX, double pivotY) {
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
        return new RotateImpl(-getAngle(), getPivotX(), getPivotY());
    }

    @Override
    protected Property[] propertiesInvalidatingCache() {
        return new Property[]{angleProperty, pivotXProperty, pivotYProperty};
    }
}
