package naga.fx.scene;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;

/**
 * @author Bruno Salmon
 */
public interface SceneRequester {

    void requestNodePeerPropertyUpdate(Node node, ObservableValue changedProperty);

    void requestNodePeerListUpdate(Node node, ObservableList changedList);

}
