package naga.toolkit.fx.scene.shape.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.fx.geom.BaseBounds;
import naga.toolkit.fx.geom.transform.BaseTransform;
import naga.toolkit.fx.scene.paint.Paint;
import naga.toolkit.fx.scene.shape.Circle;

/**
 * @author Bruno Salmon
 */
public class CircleImpl extends ShapeImpl implements Circle {

    public CircleImpl() {
    }

    public CircleImpl(double radius) {
        this(radius, null);
    }

    public CircleImpl(double radius, Paint fill) {
        this(0, 0, radius, fill);
    }

    public CircleImpl(double centerX, double centerY, double radius) {
        this(centerX, centerY, radius, null);
    }

    public CircleImpl(double centerX, double centerY, double radius, Paint fill) {
        setCenterX(centerX);
        setCenterY(centerY);
        setRadius(radius);
        if (fill != null)
            setFill(fill);
    }

    private final Property<Double> centerXProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> centerXProperty() {
        return centerXProperty;
    }

    private final Property<Double> centerYProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> centerYProperty() {
        return centerYProperty;
    }

    private final Property<Double> radiusProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> radiusProperty() {
        return radiusProperty;
    }

    @Override
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        Double radius = getRadius();
        Double centerX = getCenterX();
        Double centerY = getCenterY();
        return bounds.deriveWithNewBounds(centerX - radius, centerY - radius, 0d, centerX + radius, centerY + radius, 0d);
    }
}
