package naga.toolkit.drawing.shapes;

import naga.toolkit.drawing.shapes.impl.*;

/**
 * @author Bruno Salmon
 */
public interface DrawableFactory {

    static DrawableFactory get() {
        return DrawableFactoryImpl.SINGLETON;
    }

    default Rectangle createRectangle() {
        return new RectangleImpl();
    }

    default TextShape createText() {
        return new TextShapeImpl();
    }

    default Circle createCircle() {
        return new CircleImpl();
    }

}
