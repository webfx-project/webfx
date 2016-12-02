package naga.toolkit.fx.spi;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.Parent;

/**
 * @author Bruno Salmon
 */
public interface DrawingRequester {

    void requestParentAndChildrenViewsUpdate(Parent parent);

    void requestViewPropertyUpdate(Node node, Property changedProperty);

    void requestViewListUpdate(Node node, ObservableList changedList);

}
