package naga.toolkit.spi.nodes.gauges;

import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.properties.markers.HasMaxProperty;
import naga.toolkit.properties.markers.HasMinProperty;
import naga.toolkit.properties.markers.HasValueProperty;

/**
 * @author Bruno Salmon
 */
public interface Gauge<N> extends GuiNode<N>, HasMinProperty, HasValueProperty, HasMaxProperty {
}
