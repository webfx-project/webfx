package naga.toolkit.drawing.spi.view.mixin;

import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.view.DrawableView;
import naga.toolkit.drawing.spi.view.base.DrawableViewBase;

/**
 * @author Bruno Salmon
 */
public interface DrawableViewMixin<D extends Drawable, DV extends DrawableViewBase<D>> extends DrawableView<D> {

    DV getDrawableViewBase();

    @Override
    default void bind(D drawable, DrawingRequester drawingRequester) {
        getDrawableViewBase().bind(drawable, drawingRequester);
    }

    @Override
    default void unbind() {
        getDrawableViewBase().unbind();
    }
}
