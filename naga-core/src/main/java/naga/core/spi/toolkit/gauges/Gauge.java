package naga.core.spi.toolkit.gauges;

import naga.core.spi.toolkit.propertymarkers.HasMaxProperty;
import naga.core.spi.toolkit.propertymarkers.HasMinProperty;
import naga.core.spi.toolkit.propertymarkers.HasValueProperty;
import naga.core.spi.toolkit.node.GuiNode;

/**
 * @author Bruno Salmon
 */
public interface Gauge<N> extends GuiNode<N>, HasMinProperty, HasValueProperty, HasMaxProperty {
}
