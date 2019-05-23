package javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Paint;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.markers.HasCenterXProperty;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.markers.HasCenterYProperty;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.markers.HasRadiusProperty;

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

    private final DoubleProperty centerXProperty = new SimpleDoubleProperty(0d);
    @Override
    public DoubleProperty centerXProperty() {
        return centerXProperty;
    }

    private final DoubleProperty centerYProperty = new SimpleDoubleProperty(0d);
    @Override
    public DoubleProperty centerYProperty() {
        return centerYProperty;
    }

    private final DoubleProperty radiusProperty = new SimpleDoubleProperty(0d);
    @Override
    public DoubleProperty radiusProperty() {
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
