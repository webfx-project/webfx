package naga.toolkit.fx.shape;

import naga.toolkit.fx.paint.Paint;
import naga.toolkit.fx.shape.impl.RectangleImpl;
import naga.toolkit.properties.markers.*;

/**
 * @author Bruno Salmon
 */
public interface Rectangle extends Shape,
        HasXProperty,
        HasYProperty,
        HasWidthProperty,
        HasHeightProperty,
        HasArcWidthProperty,
        HasArcHeightProperty {

    static Rectangle create() {
        return new RectangleImpl();
    }

    static Rectangle create(double width, double height) {
        return new RectangleImpl(width, height);
    }

    static Rectangle create(double width, double height, Paint fill) {
        return new RectangleImpl(width, height, fill);
    }

    static Rectangle create(double x, double y, double width, double height) {
        return new RectangleImpl(x, y, width, height);
    }

    static Rectangle create(double x, double y, double width, double height, Paint fill) {
        return new RectangleImpl(x, y, width, height, fill);
    }

}
