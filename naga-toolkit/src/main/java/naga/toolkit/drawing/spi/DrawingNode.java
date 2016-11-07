package naga.toolkit.drawing.spi;

import naga.toolkit.properties.markers.HasHeightProperty;
import naga.toolkit.properties.markers.HasWidthProperty;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public interface DrawingNode<N> extends Drawing, GuiNode<N>,
        HasWidthProperty, // <- visual (implementation is responsible to bind it to visual width)
        HasHeightProperty // -> visual (implementation is responsible to bind the visual height to it)
{
}
