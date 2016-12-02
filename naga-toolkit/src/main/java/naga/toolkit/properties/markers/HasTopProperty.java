package naga.toolkit.properties.markers;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.Node;

/**
 * @author Bruno Salmon
 */
public interface HasTopProperty {

    Property<Node> topProperty();
    default void setTop(Node top) {
        topProperty().setValue(top);
    }
    default Node getTop() {
        return topProperty().getValue();
    }
}
