package naga.toolkit.drawing.spi;

import javafx.beans.property.Property;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.spi.view.DrawableViewFactory;

/**
 * @author Bruno Salmon
 */
public interface Drawing {

    void setDrawableViewFactory(DrawableViewFactory drawableViewFactory);

    Property<Drawable> rootDrawableProperty();
    default void setRootDrawable(Drawable rootDrawable) {
        rootDrawableProperty().setValue(rootDrawable);
    }
    default Drawable getRootDrawable() {
        return rootDrawableProperty().getValue();
    }

}
