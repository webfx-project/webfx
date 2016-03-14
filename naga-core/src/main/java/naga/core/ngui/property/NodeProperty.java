package naga.core.ngui.property;

import javafx.beans.property.Property;
import naga.core.spi.gui.GuiToolkit;

/**
 * @author Bruno Salmon
 */
public class NodeProperty<N> extends MappedProperty<naga.core.spi.gui.node.Node<N>, N> {

    public NodeProperty(Property<N> property) {
        super(property, node -> node.unwrapToToolkitNode(), fxNode -> GuiToolkit.get().wrapFromToolkitNode(fxNode));
    }
}
