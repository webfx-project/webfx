package naga.toolkit.drawing.spi.view;

import naga.toolkit.drawing.shapes.Shape;

/**
 * @author Bruno Salmon
 */
public interface ShapeView<S extends Shape> {

    void bind(S shape);

    void unbind();
}
