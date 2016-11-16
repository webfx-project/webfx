package naga.toolkit.spi.nodes.controls;

import naga.toolkit.properties.markers.HasHeightProperty;
import naga.toolkit.properties.markers.HasUrlProperty;
import naga.toolkit.properties.markers.HasWidthProperty;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public interface Image extends GuiNode, HasUrlProperty, HasWidthProperty, HasHeightProperty {
}
