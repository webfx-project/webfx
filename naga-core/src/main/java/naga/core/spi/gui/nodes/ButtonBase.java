package naga.core.spi.gui.nodes;

import naga.core.spi.gui.GuiNode;
import naga.core.spi.gui.hasproperties.HasSelectedProperty;
import naga.core.spi.gui.hasproperties.HasTextProperty;

/**
 * @author Bruno Salmon
 */
public interface ButtonBase<N> extends GuiNode<N>, HasTextProperty, HasSelectedProperty {

}
