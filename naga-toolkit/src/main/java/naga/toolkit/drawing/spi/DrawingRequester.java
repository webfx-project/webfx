package naga.toolkit.drawing.spi;

import javafx.beans.property.Property;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.shapes.DrawableParent;

/**
 * @author Bruno Salmon
 */
public interface DrawingRequester {

    void requestDrawableParentAndChildrenViewsUpdate(DrawableParent drawableParent);

    void requestDrawableViewUpdate(Drawable drawable, Property changedProperty);

}
