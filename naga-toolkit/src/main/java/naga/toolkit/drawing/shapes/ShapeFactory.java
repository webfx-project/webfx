package naga.toolkit.drawing.shapes;

import naga.toolkit.drawing.shapes.impl.CircleImpl;
import naga.toolkit.drawing.shapes.impl.RectangleImpl;
import naga.toolkit.drawing.shapes.impl.ShapeFactoryImpl;

/**
 * @author Bruno Salmon
 */
public interface ShapeFactory {

    static ShapeFactory get() {
        return ShapeFactoryImpl.SINGLETON;
    }

    default Rectangle createRectangle() {
        return new RectangleImpl();
    }

    default Circle createCircle() {
        return new CircleImpl();
    }

}
