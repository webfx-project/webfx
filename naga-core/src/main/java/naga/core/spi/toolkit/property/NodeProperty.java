package naga.core.spi.toolkit.property;

import javafx.beans.property.Property;
import naga.core.spi.toolkit.Toolkit;
import naga.core.spi.toolkit.node.GuiNode;

/**
 * @author Bruno Salmon
 */
public class NodeProperty<N> extends ConvertedProperty<GuiNode<N>, N> {

    public NodeProperty(Property<N> property) {
        super(property, node -> Toolkit.unwrapToNativeNode(node), fxNode -> Toolkit.get().wrapNativeNode(fxNode));
    }
}
