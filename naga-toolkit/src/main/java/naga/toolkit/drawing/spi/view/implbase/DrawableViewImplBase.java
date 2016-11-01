package naga.toolkit.drawing.spi.view.implbase;

import javafx.beans.property.Property;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.spi.DrawingNotifier;
import naga.toolkit.drawing.spi.view.DrawableView;
import naga.toolkit.properties.util.Properties;

/**
 * @author Bruno Salmon
 */
abstract class DrawableViewImplBase <D extends Drawable> implements DrawableView<D> {

    protected D drawable;

    @Override
    public void bind(D drawable, DrawingNotifier drawingNotifier) {
        this.drawable = drawable;
    }

    @Override
    public void unbind() {
        drawable = null;
    }

    protected static void requestRepaintDrawableOnPropertiesChange(DrawingNotifier drawingNotifier, Drawable drawable, Property... properties) {
        Properties.runOnPropertiesChange(() -> drawingNotifier.requestDrawableRepaint(drawable), properties);
    }

}
