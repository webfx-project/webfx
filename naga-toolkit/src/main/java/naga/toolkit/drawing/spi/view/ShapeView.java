package naga.toolkit.drawing.spi.view;

import naga.toolkit.drawing.shapes.Shape;
import naga.toolkit.drawing.spi.DrawingNotifier;

/**
 * @author Bruno Salmon
 */
public interface ShapeView<S extends Shape> {

    void bind(S shape, DrawingNotifier drawingNotifier);

    void unbind();
}
