package naga.fx.spi.viewer;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import naga.fx.scene.Node;
import naga.fx.scene.SceneRequester;

/**
 * @author Bruno Salmon
 */
public interface NodeViewer<N extends Node> {

    void bind(N node, SceneRequester sceneRequester);

    void unbind();

    boolean updateProperty(ObservableValue changedProperty);

    boolean updateList(ObservableList changedList);
}
