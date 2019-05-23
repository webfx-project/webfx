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


    private final DoubleProperty xProperty = new SimpleDoubleProperty(1d);

    @Override
    public DoubleProperty xProperty() {
        return xProperty;
    }

    private final DoubleProperty yProperty = new SimpleDoubleProperty(1d);
    @Override
    public DoubleProperty yProperty() {
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
