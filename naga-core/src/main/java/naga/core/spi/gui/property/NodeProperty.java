package naga.core.spi.gui.property;

import javafx.beans.property.Property;
import naga.core.spi.gui.GuiToolkit;
import naga.core.spi.gui.GuiNode;

/**
 * @author Bruno Salmon
 */
public class NodeProperty<N> extends MappedProperty<GuiNode<N>, N> {

    public NodeProperty(Property<N> property) {
        super(property, node -> node.unwrapToToolkitNode(), fxNode -> GuiToolkit.get().wrapFromToolkitNode(fxNode));
    }
}
