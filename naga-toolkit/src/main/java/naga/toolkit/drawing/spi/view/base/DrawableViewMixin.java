package naga.toolkit.drawing.spi.view.base;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.view.DrawableView;
import naga.toolkit.spi.events.MouseEvent;
import naga.toolkit.spi.events.UiEventHandler;
import naga.toolkit.transform.Transform;

import java.util.List;

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
    default boolean updateProperty(Property changedProperty) {
        return getDrawableViewBase().updateProperty(changedProperty);
    }

    @Override
    default boolean updateList(ObservableList changedList) {
        return getDrawableViewBase().updateList(changedList);
    }

    void updateTransforms(List<Transform> transforms);

    void updateOnMouseClicked(UiEventHandler<? super MouseEvent> onMouseClicked);

}
