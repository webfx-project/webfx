package naga.fx.scene;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;

/**
 * @author Bruno Salmon
 */
public interface SceneRequester {

    void requestNodeViewerPropertyUpdate(Node node, ObservableValue changedProperty);

    void requestNodeViewerListUpdate(Node node, ObservableList changedList);

}
