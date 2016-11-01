package naga.toolkit.drawing.spi.view;

import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.spi.DrawingNotifier;

/**
 * @author Bruno Salmon
 */
public interface DrawableView<D extends Drawable> {

    void bind(D drawable, DrawingNotifier drawingNotifier);

    void unbind();
}
