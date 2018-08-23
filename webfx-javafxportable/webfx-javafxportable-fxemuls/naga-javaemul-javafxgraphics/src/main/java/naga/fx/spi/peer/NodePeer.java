package naga.fx.spi.peer;

import emul.javafx.beans.value.ObservableValue;
import emul.javafx.collections.ListChangeListener;
import emul.javafx.collections.ObservableList;
import emul.javafx.scene.Node;
import naga.fx.scene.SceneRequester;

/**
 * @author Bruno Salmon
 */
public interface NodePeer<N extends Node> {

    void bind(N node, SceneRequester sceneRequester);

    void unbind();

    N getNode();

    boolean updateProperty(ObservableValue changedProperty);

    boolean updateList(ObservableList list, ListChangeListener.Change change);

    void requestFocus();

    default boolean isTreeVisible() {
        return true;
    }
}
