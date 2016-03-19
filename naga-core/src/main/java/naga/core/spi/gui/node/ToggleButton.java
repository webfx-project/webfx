package naga.core.spi.gui.node;

import naga.core.spi.gui.properties.HasSelectedProperty;

/**
 * @author Bruno Salmon
 */
public interface ToggleButton<N> extends ButtonBase<N>, HasSelectedProperty, UserInputNode<Boolean, N> {

}