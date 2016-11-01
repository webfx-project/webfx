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
    }

    @Override
    public void unbind() {
        drawable = null;
    }

    protected void requestDrawableViewUpdateOnPropertiesChange(DrawingRequester drawingRequester, D drawable, Property... properties) {
        Properties.runOnPropertiesChange(() -> this.requestDrawableViewUpdate(drawingRequester, drawable), properties);
    }

    protected void requestDrawableViewUpdate(DrawingRequester drawingRequester, D drawable) {
        drawingRequester.requestDrawableViewUpdate(drawable);
    }

}
