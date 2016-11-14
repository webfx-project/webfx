package naga.toolkit.drawing.spi;

import javafx.beans.property.Property;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.spi.view.DrawableViewFactory;

/**
 * @author Bruno Salmon
 */
public interface DrawingMixin extends Drawing {

    Drawing getDrawing();

    default void setDrawableViewFactory(DrawableViewFactory drawableViewFactory) {
        getDrawing().setDrawableViewFactory(drawableViewFactory);
    }

    @Override
    default Property<Drawable> rootDrawableProperty() {
        return getDrawing().rootDrawableProperty();
    }

    @Override
    default void setRootDrawable(Drawable rootDrawable) {
        getDrawing().setRootDrawable(rootDrawable);
    }

    @Override
    default Drawable getRootDrawable() {
        return getDrawing().getRootDrawable();
    }
}
