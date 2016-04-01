package naga.core.spi.gui.nodes;

import naga.core.spi.gui.GuiNode;
import naga.core.spi.gui.hasproperties.HasPlaceholderProperty;
import naga.core.spi.gui.hasproperties.HasTextProperty;

/**
 * @author Bruno Salmon
 */
public interface TextField<N> extends GuiNode<N>, HasTextProperty, HasPlaceholderProperty {

}