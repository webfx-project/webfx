package naga.toolkit.drawing.spi;

import javafx.collections.ObservableList;
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
    default ObservableList<Drawable> getDrawableChildren() {
        return getDrawing().getDrawableChildren();
    }
}
