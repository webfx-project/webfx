package naga.toolkit.drawing.spi.view;

import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.spi.DrawingRequester;

/**
 * @author Bruno Salmon
 */
public interface DrawableView<D extends Drawable> {

    void bind(D drawable, DrawingRequester drawingRequester);

    void unbind();
}
