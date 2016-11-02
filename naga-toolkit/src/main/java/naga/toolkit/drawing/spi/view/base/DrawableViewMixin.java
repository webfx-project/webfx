package naga.toolkit.drawing.spi.view.base;

import javafx.beans.property.Property;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.view.DrawableView;

/**
 * @author Bruno Salmon
 */
public interface DrawableViewMixin
        <D extends Drawable, DV extends DrawableViewBase<D, DV, DM>, DM extends DrawableViewMixin<D, DV, DM>>
        extends DrawableView<D> {

    DV getDrawableViewBase();

    @Override
    default void bind(D drawable, DrawingRequester drawingRequester) {
        getDrawableViewBase().bind(drawable, drawingRequester);
    }

    @Override
    default void unbind() {
        getDrawableViewBase().unbind();
    }

    @Override
    default boolean update(Property changedProperty) {
        return getDrawableViewBase().update(changedProperty);
    }
}
