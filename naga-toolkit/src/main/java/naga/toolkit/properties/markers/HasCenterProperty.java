package naga.toolkit.properties.markers;

import javafx.beans.property.Property;
import naga.toolkit.drawing.scene.Node;

/**
 * @author Bruno Salmon
 */
public interface HasCenterProperty {

    Property<Node> centerProperty();
    default void setCenter(Node center) {
        centerProperty().setValue(center);
    }
    default Node getCenter() {
        return centerProperty().getValue();
    }
}
