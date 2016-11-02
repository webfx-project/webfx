package naga.toolkit.drawing.spi;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.shapes.DrawableParent;

/**
 * @author Bruno Salmon
 */
public interface DrawingRequester {

    void requestDrawableParentAndChildrenViewsUpdate(DrawableParent drawableParent);

    void requestDrawableViewUpdateProperty(Drawable drawable, Property changedProperty);

    void requestDrawableViewUpdateList(Drawable drawable, ObservableList changedList);

}
