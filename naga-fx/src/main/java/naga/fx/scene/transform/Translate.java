package naga.fx.scene.transform;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.fx.sun.geom.Point2D;
import naga.fx.properties.markers.HasXProperty;
import naga.fx.properties.markers.HasYProperty;

/**
 * @author Bruno Salmon
 */
public class Translate extends Transform implements
        HasXProperty,
        HasYProperty {

    public Translate() {
    }

    public Translate(double x) {
        this(x, 0d);
    }

    public Translate(double x, double y) {
        setX(x);
        setY(y);
    }

    private final Property<Double> xProperty = new SimpleObjectProperty<>(0d);

    public static Translate create() {
        return new Translate();
    }

    public static Translate create(double x) {
        return new Translate(x);
    }

    public static Translate create(double x, double y) {
        return new Translate(x, y);
    }

    @Override
    public Property<Double> xProperty() {
        return xProperty;
    }

    private final Property<Double> yProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> yProperty() {
        return yProperty;
    }

    @Override
    public Point2D transform(double x, double y) {
        return new Point2D(x + getX(), y + getY());
    }

    @Override
    public Transform createInverse() {
        return new Translate(-getX(), -getY());
    }

    @Override
    protected Property[] propertiesInvalidatingCache() {
        return new Property[]{xProperty, yProperty};
    }

    @Override
    public Affine toAffine() {
        return new Affine(1, 0, 0, 1, getX(), getY());
    }
}
