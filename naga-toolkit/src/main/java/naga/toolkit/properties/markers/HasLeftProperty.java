package naga.toolkit.properties.markers;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.Node;

/**
 * @author Bruno Salmon
 */
public interface HasLeftProperty {

    Property<Node> leftProperty();
    default void setLeft(Node left) {
        leftProperty().setValue(left);
    }
    default Node getLeft() {
        return leftProperty().getValue();
    }
}
