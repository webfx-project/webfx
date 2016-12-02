package naga.toolkit.properties.markers;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.Node;

/**
 * @author Bruno Salmon
 */
public interface HasRightProperty {

    Property<Node> rightProperty();
    default void setRight(Node right) {
        rightProperty().setValue(right);
    }
    default Node getRight() {
        return rightProperty().getValue();
    }
}
