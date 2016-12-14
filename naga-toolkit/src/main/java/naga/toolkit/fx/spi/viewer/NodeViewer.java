package naga.toolkit.fx.spi.viewer;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.spi.DrawingRequester;

/**
 * @author Bruno Salmon
 */
public interface NodeViewer<N extends Node> {

    void bind(N node, DrawingRequester drawingRequester);

    void unbind();

    boolean updateProperty(ObservableValue changedProperty);

    boolean updateList(ObservableList changedList);
}
