package naga.toolkit.drawing.spi.view.base;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import naga.toolkit.drawing.shapes.BlendMode;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.view.DrawableView;
import naga.toolkit.effect.Effect;
import naga.toolkit.spi.events.MouseEvent;
import naga.toolkit.spi.events.UiEventHandler;
import naga.toolkit.transform.Transform;

import java.util.Collection;
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

    void updateOnMouseClicked(UiEventHandler<? super MouseEvent> onMouseClicked);

    void updateVisible(Boolean visible);

    void updateOpacity(Double opacity);

    void updateClip(Drawable clip);

    void updateBlendMode(BlendMode blendMode);

    void updateEffect(Effect effect);

    default void updateLayoutX(Double layoutX) {
        updateLocalToParentTransforms(getDrawableViewBase().getDrawable().localToParentTransforms());
    }

    default void updateLayoutY(Double layoutY) {
        updateLocalToParentTransforms(getDrawableViewBase().getDrawable().localToParentTransforms());
    }

    default void updateTransforms(List<Transform> transforms) {
        updateLocalToParentTransforms(getDrawableViewBase().getDrawable().localToParentTransforms());
    }

    void updateLocalToParentTransforms(Collection<Transform> localToParentTransforms);
}
