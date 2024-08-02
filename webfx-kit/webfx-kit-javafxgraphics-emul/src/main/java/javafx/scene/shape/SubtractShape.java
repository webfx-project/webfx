package javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import dev.webfx.kit.registry.javafxgraphics.JavaFxGraphicsRegistry;

/**
 * SubtractShape is a pure WebFX class created by Shape.subtract() and the only supported usage so far is clipping.
 *
 * @author Bruno Salmon
 */
public class SubtractShape extends Shape {

    private final Shape shape1;
    private final Shape shape2;

    public SubtractShape(Shape shape1, Shape shape2) {
        this.shape1 = shape1;
        this.shape2 = shape2;
    }

    @Override
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        return shape1.impl_computeGeomBounds(bounds, tx);
    }

    public Shape getShape1() {
        return shape1;
    }

    public Shape getShape2() {
        return shape2;
    }

    static {
        JavaFxGraphicsRegistry.registerSubtractShape();
    }
}
