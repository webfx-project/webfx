package naga.framework.activity.client;

import javafx.beans.property.Property;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public interface HasMountNodeProperty {

    Property<GuiNode> mountNodeProperty();
    default void setMountNode(GuiNode node) { mountNodeProperty().setValue(node); }
    default GuiNode getMountNode() { return mountNodeProperty().getValue(); }

}
