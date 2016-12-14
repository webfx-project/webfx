package naga.toolkit.fx.spi;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.Parent;

/**
 * @author Bruno Salmon
 */
public interface DrawingRequester {

    void requestParentAndChildrenViewsUpdate(Parent parent);

    void requestViewPropertyUpdate(Node node, ObservableValue changedProperty);

    void requestViewListUpdate(Node node, ObservableList changedList);

}
