package naga.toolkit.properties.conversion;

import javafx.beans.property.Property;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public class NodeProperty<N> extends ConvertedProperty<GuiNode<N>, N> {

    public NodeProperty(Property<N> property) {
        super(property, node -> Toolkit.unwrapToNativeNode(node), fxNode -> Toolkit.get().wrapNativeNode(fxNode));
    }
}
