package naga.toolkit.transform.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.drawing.geom.Point2D;
import naga.toolkit.transform.Scale;
import naga.toolkit.transform.Transform;

/**
 * @author Bruno Salmon
 */
public class ScaleImpl extends TransformImpl implements Scale {

    public ScaleImpl() {
    }

    public ScaleImpl(double x, double y) {
        setX(x);
        setY(y);
    }

    private final Property<Double> xProperty = new SimpleObjectProperty<>(1d);
    @Override
    public Property<Double> xProperty() {
        return xProperty;
    }

    private final Property<Double> yProperty = new SimpleObjectProperty<>(1d);
    @Override
    public Property<Double> yProperty() {
        return yProperty;
    }

    @Override
    public Point2D transform(double x, double y) {
        return Point2D.create(x * getX(), y * getY());
    }

    @Override
    public Transform createInverse() {
        return new ScaleImpl(1 / getX(), 1 / getY());
    }

    @Override
    protected Property[] propertiesInvalidatingCache() {
        return new Property[]{xProperty, yProperty};
    }
}
