package naga.toolkit.properties.markers;

import javafx.beans.property.Property;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public interface HasNodeProperty {

    Property<GuiNode> nodeProperty();
    default HasNodeProperty setNode(GuiNode node) { nodeProperty().setValue(node); return this; }
    default GuiNode getNode() { return nodeProperty().getValue(); }
}
