package naga.fx.properties.markers;

import emul.javafx.beans.property.Property;
import emul.javafx.scene.Node;

/**
 * @author Bruno Salmon
 */
public interface HasNodeProperty {

    Property<Node> nodeProperty();
    default HasNodeProperty setNode(Node node) { nodeProperty().setValue(node); return this; }
    default Node getNode() { return nodeProperty().getValue(); }
}
