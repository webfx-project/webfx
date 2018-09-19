package emul.javafx.scene.transform;

import emul.javafx.beans.property.Property;
import emul.javafx.beans.property.SimpleObjectProperty;
import emul.com.sun.javafx.geom.Point2D;
import webfx.fxkits.core.mapper.spi.impl.peer.markers.HasXProperty;
import webfx.fxkits.core.mapper.spi.impl.peer.markers.HasYProperty;

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
        return new Point2D((float) (x + getX()), (float) (y + getY()));
    }

    @Override
    public Transform createInverse() {
        return new Translate(-getX(), -getY());
    }

    @Override
    public Property[] propertiesInvalidatingCache() {
        return new Property[]{xProperty, yProperty};
    }

    @Override
    public Affine toAffine() {
        return new Affine(1, 0, 0, 1, getX(), getY());
    }
}
