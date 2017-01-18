package emul.javafx.scene.transform;

import emul.javafx.beans.property.Property;
import emul.javafx.beans.property.SimpleObjectProperty;
import emul.com.sun.javafx.geom.Point2D;
import naga.fx.properties.markers.HasXProperty;
import naga.fx.properties.markers.HasYProperty;

/**
 * @author Bruno Salmon
 */
public class Scale extends Transform implements
        HasXProperty,
        HasYProperty {

    public Scale() {
    }

    public Scale(double x, double y) {
        setX(x);
        setY(y);
    }

    private final Property<Double> xProperty = new SimpleObjectProperty<>(1d);

    public static Scale create() {
        return new Scale();
    }

    public static Scale create(double x, double y) {
        return new Scale(x, y);
    }

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
        return new Point2D((float) (x * getX()), (float) (y * getY()));
    }

    @Override
    public Transform createInverse() {
        return new Scale(1 / getX(), 1 / getY());
    }

    @Override
    protected Property[] propertiesInvalidatingCache() {
        return new Property[]{xProperty, yProperty};
    }

    @Override
    public Affine toAffine() {
        return new Affine(getX(), 0, 0, getY(), 0, 0);
    }
}
