package naga.toolkit.properties.markers;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.Node;

/**
 * @author Bruno Salmon
 */
public interface HasBottomProperty {

    Property<Node> bottomProperty();
    default void setBottom(Node bottom) {
        bottomProperty().setValue(bottom);
    }
    default Node getBottom() {
        return bottomProperty().getValue();
    }
}
