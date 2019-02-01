package javafx.scene.transform;

import com.sun.javafx.geom.Point2D;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import webfx.fxkit.mapper.spi.impl.peer.markers.HasXProperty;
import webfx.fxkit.mapper.spi.impl.peer.markers.HasYProperty;

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

    private final DoubleProperty xProperty = new SimpleDoubleProperty(0d);

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
    public DoubleProperty xProperty() {
        return xProperty;
    }

    private final DoubleProperty yProperty = new SimpleDoubleProperty(0d);
    @Override
    public DoubleProperty yProperty() {
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
