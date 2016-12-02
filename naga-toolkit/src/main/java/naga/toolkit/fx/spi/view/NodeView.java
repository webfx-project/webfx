package naga.toolkit.fx.spi.view;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.spi.DrawingRequester;

/**
 * @author Bruno Salmon
 */
public interface NodeView<N extends Node> {

    void bind(N node, DrawingRequester drawingRequester);

    void unbind();

    boolean updateProperty(Property changedProperty);

    boolean updateList(ObservableList changedList);
}
