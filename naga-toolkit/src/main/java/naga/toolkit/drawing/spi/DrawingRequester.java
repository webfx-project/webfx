package naga.toolkit.drawing.spi;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import naga.toolkit.drawing.shapes.Node;
import naga.toolkit.drawing.shapes.Parent;

/**
 * @author Bruno Salmon
 */
public interface DrawingRequester {

    void requestParentAndChildrenViewsUpdate(Parent parent);

    void requestViewPropertyUpdate(Node node, Property changedProperty);

    void requestViewListUpdate(Node node, ObservableList changedList);

}
