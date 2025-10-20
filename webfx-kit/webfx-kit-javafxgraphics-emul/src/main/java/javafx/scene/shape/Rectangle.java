package javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Paint;
import dev.webfx.kit.mapper.peers.javafxgraphics.markers.*;
import dev.webfx.kit.registry.javafxgraphics.JavaFxGraphicsRegistry;

/**
 * @author Bruno Salmon
 */
public class Rectangle extends Shape implements
        HasXProperty,
        HasYProperty,
        HasWidthProperty,
        HasHeightProperty,
        HasArcWidthProperty,
        HasArcHeightProperty {

    public Rectangle() {
    }

    public Rectangle(double width, double height) {
        this(width, height, null);
    }

    public Rectangle(double width, double height, Paint fill) {
        this(0, 0, width, height, fill);
    }

    public Rectangle(double x, double y, double width, double height) {
        this(x, y, width, height, null);
    }

    public Rectangle(double x, double y, double width, double height, Paint fill) {
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        if (fill != null)
            setFill(fill);
    }

    private final DoubleProperty xProperty = new SimpleDoubleProperty(0d);
    @Override
    public DoubleProperty xProperty() {
        return xProperty;
    }

    private final DoubleProperty yProperty = new SimpleDoubleProperty(0d);
    @Override
    public DoubleProperty yProperty() {
        return yProperty;
    }

    private final SimpleDoubleProperty widthProperty = new SimpleDoubleProperty(0d);
    @Override
    public DoubleProperty widthProperty() {
        return widthProperty;
    }

    private final DoubleProperty heightProperty = new SimpleDoubleProperty(0d);
    @Override
    public DoubleProperty heightProperty() {
        return heightProperty;
    }

    private final DoubleProperty arcWidthProperty = new SimpleDoubleProperty(0d);
    @Override
    public DoubleProperty arcWidthProperty() {
        return arcWidthProperty;
    }

    private final DoubleProperty arcHeightProperty = new SimpleDoubleProperty(0d);
    @Override
    public DoubleProperty arcHeightProperty() {
        return arcHeightProperty;
    }

    @Override
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        bounds.setBoundsAndSort((float) getX(), (float) getY(), 0, (float) (getX() + getWidth()), (float) (getY() + getHeight()), 0);
        return bounds;
    }

    static {
        JavaFxGraphicsRegistry.registerRectangle();
    }
}
