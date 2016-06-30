package naga.core.spi.toolkit.gauges;

import naga.core.spi.toolkit.hasproperties.HasMaxProperty;
import naga.core.spi.toolkit.hasproperties.HasMinProperty;
import naga.core.spi.toolkit.hasproperties.HasValueProperty;
import naga.core.spi.toolkit.node.GuiNode;

/**
 * @author Bruno Salmon
 */
public interface Gauge<N> extends GuiNode<N>, HasMinProperty, HasValueProperty, HasMaxProperty {
}
