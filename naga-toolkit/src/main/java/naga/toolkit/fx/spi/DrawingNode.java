package naga.toolkit.fx.spi;

import naga.toolkit.properties.markers.HasHeightProperty;
import naga.toolkit.properties.markers.HasWidthProperty;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public interface DrawingNode extends Drawing, GuiNode,
        HasWidthProperty, // <- visual (implementation is responsible to bind it to visual width)
        HasHeightProperty // -> visual (implementation is responsible to bind the visual height to it)
{
}
