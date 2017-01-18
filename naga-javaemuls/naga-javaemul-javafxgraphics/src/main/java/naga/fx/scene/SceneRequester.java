package naga.fx.scene;

import emul.javafx.beans.value.ObservableValue;
import emul.javafx.collections.ObservableList;
import emul.javafx.scene.Node;

/**
 * @author Bruno Salmon
 */
public interface SceneRequester {

    void requestNodePeerPropertyUpdate(Node node, ObservableValue changedProperty);

    void requestNodePeerListUpdate(Node node, ObservableList changedList);

}
