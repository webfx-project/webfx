package webfx.framework.client.activity.impl.elementals.view;

import javafx.beans.property.Property;
import javafx.scene.Node;

/**
 * @author Bruno Salmon
 */
public interface HasNodeProperty {

    Property<Node> nodeProperty();
    default HasNodeProperty setNode(Node node) { nodeProperty().setValue(node); return this; }
    default Node getNode() { return nodeProperty().getValue(); }
}
