package naga.toolkit.properties.markers;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.Node;

/**
 * @author Bruno Salmon
 */
public interface HasNodeProperty {

    Property<Node> nodeProperty();
    default HasNodeProperty setNode(Node node) { nodeProperty().setValue(node); return this; }
    default Node getNode() { return nodeProperty().getValue(); }
}
