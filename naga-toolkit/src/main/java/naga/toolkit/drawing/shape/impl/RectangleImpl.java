package naga.toolkit.drawing.shape.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.drawing.geom.BaseBounds;
import naga.toolkit.drawing.geom.transform.BaseTransform;
import naga.toolkit.drawing.paint.Paint;
import naga.toolkit.drawing.shape.Rectangle;

/**
 * @author Bruno Salmon
 */
public class RectangleImpl extends ShapeImpl implements Rectangle {

    public RectangleImpl() {
    }

    public RectangleImpl(double width, double height) {
        this(width, height, null);
    }

    public RectangleImpl(double width, double height, Paint fill) {
        this(0, 0, width, height, fill);
    }

    public RectangleImpl(double x, double y, double width, double height) {
        this(x, y, width, height, null);
    }

    public RectangleImpl(double x, double y, double width, double height, Paint fill) {
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        if (fill != null)
            setFill(fill);
    }

    private final Property<Double> xProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> xProperty() {
        return xProperty;
    }

    private final Property<Double> yProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> yProperty() {
        return yProperty;
    }

    private final Property<Double> widthProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> widthProperty() {
        return widthProperty;
    }

    private final Property<Double> heightProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> heightProperty() {
        return heightProperty;
    }

    private final Property<Double> arcWidthProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> arcWidthProperty() {
        return arcWidthProperty;
    }

    private final Property<Double> arcHeightProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> arcHeightProperty() {
        return arcHeightProperty;
    }

    @Override
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        bounds.setBoundsAndSort(getX(), getY(), 0, getX() + getWidth(), getY() + getHeight(), 0);
        return bounds;
    }
}
