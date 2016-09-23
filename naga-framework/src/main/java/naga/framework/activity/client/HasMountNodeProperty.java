package naga.framework.activity.client;

import javafx.beans.property.Property;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public interface HasMountNodeProperty<N> {

    Property<GuiNode<N>> mountNodeProperty();
    default HasMountNodeProperty setMountNode(GuiNode node) { mountNodeProperty().setValue(node); return this; }
    default GuiNode<N> getMountNode() { return mountNodeProperty().getValue(); }

}
