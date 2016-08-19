package naga.toolkit.properties.conversion;

import javafx.beans.property.Property;
import naga.commons.util.function.Converter;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public class NodeProperty<N> extends ConvertedProperty<GuiNode<N>, N> {

    public NodeProperty(Property<N> property) {
        this(property, node -> node);
    }

    public NodeProperty(Property<N> property, Converter<N, N> unwrapConverter) {
        super(property, node -> unwrapConverter.convert(Toolkit.unwrapToNativeNode(node)), Toolkit.get()::wrapNativeNode);
    }
}
