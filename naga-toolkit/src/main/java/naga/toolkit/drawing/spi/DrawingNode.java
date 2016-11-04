package naga.toolkit.drawing.spi;

import naga.toolkit.properties.markers.HasWidthProperty;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public interface DrawingNode<N> extends Drawing, GuiNode<N>,
        HasWidthProperty // to be managed by the implementation so it always reflects the actual width displayed on the screen
{
}
