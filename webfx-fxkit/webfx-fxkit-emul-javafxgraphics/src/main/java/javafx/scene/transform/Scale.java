package javafx.scene.transform;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import com.sun.javafx.geom.Point2D;
import webfx.fxkit.mapper.spi.impl.peer.markers.HasXProperty;
import webfx.fxkit.mapper.spi.impl.peer.markers.HasYProperty;

/**
 * @author Bruno Salmon
 */
public class Scale extends PivotTransform implements
        HasXProperty,
        HasYProperty {

    public Scale() {
    }

    public Scale(double x, double y) {
        this(x, y, 0, 0);
    }

    public Scale(double x, double y, double pivotX, double pivotY) {
        super(pivotX, pivotY);
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
        double pivotX = getPivotX();
        double pivotY = getPivotY();
        return new Point2D((float) (pivotX + (x - pivotX) * getX()), (float) (pivotY + (y - pivotY) * getY()));
    }

    @Override
    public Transform createInverse() {
        return new Scale(1 / getX(), 1 / getY(), getPivotX(), getPivotY());
    }

    @Override
    public Property[] propertiesInvalidatingCache() {
        return new Property[]{xProperty, yProperty, pivotXProperty, pivotYProperty};
    }

    @Override
    public Affine toAffine() {
        return new Affine(getX(), 0, 0, getY(), (1 - getX()) * getPivotX(), (1 - getY()) * getPivotY());
    }
}
