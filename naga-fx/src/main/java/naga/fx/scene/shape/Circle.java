package naga.fx.scene.shape;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import naga.fx.scene.paint.Paint;
import naga.fx.properties.markers.HasCenterXProperty;
import naga.fx.properties.markers.HasCenterYProperty;
import naga.fx.properties.markers.HasRadiusProperty;

/**
 * @author Bruno Salmon
 */
public class Circle extends Shape implements
        HasCenterXProperty,
        HasCenterYProperty,
        HasRadiusProperty {

    public Circle() {
    }

    public Circle(double radius) {
        this(radius, null);
    }

    public Circle(double radius, Paint fill) {
        this(0, 0, radius, fill);
    }

    public Circle(double centerX, double centerY, double radius) {
        this(centerX, centerY, radius, null);
    }

    public Circle(double centerX, double centerY, double radius, Paint fill) {
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
        return bounds.deriveWithNewBounds((float) (centerX - radius), (float) (centerY - radius), 0, (float) (centerX + radius), (float) (centerY + radius), 0);
    }
}
