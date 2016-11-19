package naga.toolkit.drawing.spi.view.base;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import naga.commons.util.Arrays;
import naga.commons.util.collection.Collections;
import naga.commons.util.function.Consumer;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.impl.DrawingImpl;
import naga.toolkit.drawing.spi.impl.canvas.CanvasDrawingImpl;
import naga.toolkit.drawing.spi.view.DrawableView;
import naga.toolkit.transform.Rotate;
import naga.toolkit.transform.Scale;
import naga.toolkit.transform.Transform;
import naga.toolkit.transform.Translate;
import naga.toolkit.util.ObservableLists;
import naga.toolkit.util.Properties;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public abstract class DrawableViewBase
        <D extends Drawable, DV extends DrawableViewBase<D, DV, DM>, DM extends DrawableViewMixin<D, DV, DM>>
        implements DrawableView<D> {

    protected D drawable;
    protected DM mixin;

    public void setMixin(DM mixin) {
        this.mixin = mixin;
    }

    @Override
    public void bind(D drawable, DrawingRequester drawingRequester) {
        this.drawable = drawable;
        requestUpdateProperty(drawingRequester, null);
        requestUpdateList(drawingRequester, null);
        requestUpdateOnListChange(drawingRequester, drawable.getTransforms());
        requestUpdateOnPropertiesChange(drawingRequester,
                drawable.visibleProperty(),
                drawable.opacityProperty(),
                drawable.clipProperty(),
                drawable.blendModeProperty(),
                drawable.effectProperty(),
                drawable.layoutXProperty(),
                drawable.layoutYProperty(),
                drawable.onMouseClickedProperty());
    }

    @Override
    public void unbind() {
        drawable = null;
    }

    public D getDrawable() {
        return drawable;
    }

    void requestUpdateOnPropertiesChange(DrawingRequester drawingRequester, Property... properties) {
        Properties.runOnPropertiesChange(property -> requestUpdateProperty(drawingRequester, property), properties);
    }

    private void requestUpdateProperty(DrawingRequester drawingRequester, Property changedProperty) {
        drawingRequester.requestDrawableViewUpdateProperty(drawable, changedProperty);
    }

    void requestUpdateOnListsChange(DrawingRequester drawingRequester, ObservableList... lists) {
        Arrays.forEach(lists, list -> requestUpdateOnListChange(drawingRequester, list));
    }

    void requestUpdateOnListChange(DrawingRequester drawingRequester, ObservableList list) {
        ObservableLists.runOnListChange(() -> requestUpdateList(drawingRequester, list), list);
    }

    void requestUpdateList(DrawingRequester drawingRequester, ObservableList changedList) {
        drawingRequester.requestDrawableViewUpdateList(drawable, changedList);
    }

    @Override
    public boolean updateProperty(Property changedProperty) {
        return updateProperty(drawable.onMouseClickedProperty(), changedProperty, mixin::updateOnMouseClicked)
                || updateProperty(drawable.visibleProperty(), changedProperty, mixin::updateVisible)
                || updateProperty(drawable.opacityProperty(), changedProperty, mixin::updateOpacity)
                || updateProperty(drawable.clipProperty(), changedProperty, mixin::updateClip)
                || updateProperty(drawable.blendModeProperty(), changedProperty, mixin::updateBlendMode)
                || updateProperty(drawable.effectProperty(), changedProperty, mixin::updateEffect)
                || updateProperty(drawable.layoutXProperty(), changedProperty, mixin::updateLayoutX)
                || updateProperty(drawable.layoutYProperty(), changedProperty, mixin::updateLayoutY);
    }

    @Override
    public boolean updateList(ObservableList changedList) {
        return updateList(drawable.getTransforms(), changedList, this::updateTransforms);
    }

    private void updateTransforms(List<Transform> transforms) {
        mixin.updateTransforms(transforms);
        Collections.forEach(transforms, this::bindTransform);
    }

    private void bindTransform(Transform transform) {
        DrawingImpl drawing = DrawingImpl.getThreadLocalDrawing();
        Property[] properties = null;
        if (transform instanceof Translate) {
            Translate translate = (Translate) transform;
            properties = new Property[]{translate.xProperty(), translate.yProperty()};
        } else if (transform instanceof Rotate) {
            Rotate rotate = (Rotate) transform;
            properties = new Property[]{rotate.angleProperty(), rotate.pivotXProperty(), rotate.pivotYProperty()};
        } else if (transform instanceof Scale) {
            Scale scale = (Scale) transform;
            properties = new Property[]{scale.xProperty(), scale.yProperty()};
        }
        if (properties != null)
            Properties.runOnPropertiesChange(arg -> {
                mixin.updateTransforms(drawable.getTransforms());
                if (drawing instanceof CanvasDrawingImpl)
                    ((CanvasDrawingImpl) drawing).requestCanvasRepaint();
            }, properties);
    }


    <T> boolean updateProperty(Property<T> property, Property changedProperty, Consumer<T> updater) {
        boolean hitChangedProperty = property == changedProperty;
        if (hitChangedProperty || changedProperty == null)
            updater.accept(property.getValue());
        return hitChangedProperty;
    }

    <T> boolean updateList(ObservableList<T> list, ObservableList changedList, Consumer<List<T>> updater) {
        boolean hitChangedProperty = list == changedList;
        if (hitChangedProperty || changedList == null)
            updater.accept(list);
        return hitChangedProperty;
    }
}
