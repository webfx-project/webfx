package naga.toolkit.drawing.spi.view;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import naga.toolkit.drawing.shapes.Node;
import naga.toolkit.drawing.spi.DrawingRequester;

/**
 * @author Bruno Salmon
 */
public interface NodeView<N extends Node> {

    void bind(N node, DrawingRequester drawingRequester);

    void unbind();

    boolean updateProperty(Property changedProperty);

    boolean updateList(ObservableList changedList);
}
