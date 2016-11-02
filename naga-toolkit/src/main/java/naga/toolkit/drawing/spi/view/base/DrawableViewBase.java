package naga.toolkit.drawing.spi.view.base;

import javafx.beans.property.Property;
import naga.commons.util.function.Consumer;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.view.DrawableView;
import naga.toolkit.util.Properties;

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
        requestUpdate(drawingRequester, null);
    }

    @Override
    public void unbind() {
        drawable = null;
    }

    public D getDrawable() {
        return drawable;
    }

    void requestUpdateOnPropertiesChange(DrawingRequester drawingRequester, Property... properties) {
        Properties.runOnPropertiesChange(property -> requestUpdate(drawingRequester, property), properties);
    }

    private void requestUpdate(DrawingRequester drawingRequester, Property changedProperty) {
        drawingRequester.requestDrawableViewUpdate(drawable, changedProperty);
    }

    @Override
    public boolean update(Property changedProperty) {
        return false;
    }

    <T> boolean updateProperty(Property<T> property, Property changedProperty, Consumer<T> updater) {
        boolean hitChangedProperty = property == changedProperty;
        if (hitChangedProperty || changedProperty == null)
            updater.accept(property.getValue());
        return hitChangedProperty;
    }
}
