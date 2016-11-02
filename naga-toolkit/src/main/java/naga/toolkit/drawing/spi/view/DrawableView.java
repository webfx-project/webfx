package naga.toolkit.drawing.spi.view;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.spi.DrawingRequester;

/**
 * @author Bruno Salmon
 */
public interface DrawableView<D extends Drawable> {

    void bind(D drawable, DrawingRequester drawingRequester);

    void unbind();

    boolean updateProperty(Property changedProperty);

    boolean updateList(ObservableList changedList);
}
