package naga.framework.activity.client;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.Node;

/**
 * @author Bruno Salmon
 */
public interface HasMountNodeProperty {

    Property<Node> mountNodeProperty();
    default void setMountNode(Node node) { mountNodeProperty().setValue(node); }
    default Node getMountNode() { return mountNodeProperty().getValue(); }

}
