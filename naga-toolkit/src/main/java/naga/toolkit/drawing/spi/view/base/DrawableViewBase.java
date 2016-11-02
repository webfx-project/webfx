package naga.toolkit.drawing.spi.view.base;

import javafx.beans.property.Property;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.view.DrawableView;
import naga.toolkit.util.Properties;

/**
 * @author Bruno Salmon
 */
public abstract class DrawableViewBase<D extends Drawable> implements DrawableView<D> {

    protected D drawable;

    @Override
    public void bind(D drawable, DrawingRequester drawingRequester) {
        this.drawable = drawable;
        requestDrawableViewUpdate(drawingRequester, null);
    }

    @Override
    public void unbind() {
        drawable = null;
    }

    public D getDrawable() {
        return drawable;
    }

    protected void requestDrawableViewUpdateOnPropertiesChange(DrawingRequester drawingRequester, Property... properties) {
        Properties.runOnPropertiesChange(property -> requestDrawableViewUpdate(drawingRequester, property), properties);
    }

    protected void requestDrawableViewUpdate(DrawingRequester drawingRequester, Property changedProperty) {
        drawingRequester.requestDrawableViewUpdate(drawable, changedProperty);
    }

}
