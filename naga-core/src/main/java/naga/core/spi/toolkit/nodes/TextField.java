package naga.core.spi.toolkit.nodes;

import naga.core.spi.toolkit.GuiNode;
import naga.core.spi.toolkit.hasproperties.HasPlaceholderProperty;
import naga.core.spi.toolkit.hasproperties.HasTextProperty;

/**
 * @author Bruno Salmon
 */
public interface TextField<N> extends GuiNode<N>, HasTextProperty, HasPlaceholderProperty {

}