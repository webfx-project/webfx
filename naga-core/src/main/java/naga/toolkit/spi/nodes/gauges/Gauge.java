package naga.toolkit.spi.nodes.gauges;

import naga.toolkit.spi.properties.markers.HasMaxProperty;
import naga.toolkit.spi.properties.markers.HasMinProperty;
import naga.toolkit.spi.properties.markers.HasValueProperty;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public interface Gauge<N> extends GuiNode<N>, HasMinProperty, HasValueProperty, HasMaxProperty {
}
