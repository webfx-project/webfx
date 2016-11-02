package naga.toolkit.drawing.spi.view.implbase;

import javafx.beans.property.Property;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.view.DrawableView;
import naga.toolkit.util.Properties;

/**
 * @author Bruno Salmon
 */
abstract class DrawableViewImplBase <D extends Drawable> implements DrawableView<D> {

    protected D drawable;

    @Override
    public void bind(D drawable, DrawingRequester drawingRequester) {
        this.drawable = drawable;
        requestDrawableViewUpdate(drawable, null, drawingRequester);
    }

    @Override
    public void unbind() {
        drawable = null;
    }

    protected void requestDrawableViewUpdateOnPropertiesChange(D drawable, DrawingRequester drawingRequester, Property... properties) {
        Properties.runOnPropertiesChange(property -> this.requestDrawableViewUpdate(drawable, property, drawingRequester), properties);
    }

    protected void requestDrawableViewUpdate(D drawable, Property changedProperty, DrawingRequester drawingRequester) {
        drawingRequester.requestDrawableViewUpdate(drawable, changedProperty);
    }

}
