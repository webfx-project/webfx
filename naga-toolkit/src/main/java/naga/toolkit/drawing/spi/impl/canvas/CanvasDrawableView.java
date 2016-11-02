package naga.toolkit.drawing.spi.impl.canvas;

import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.spi.view.DrawableView;

/**
 * @author Bruno Salmon
 */
public interface CanvasDrawableView
        <D extends Drawable, CC>
        extends DrawableView<D> {

    void paint(CC c);

}
