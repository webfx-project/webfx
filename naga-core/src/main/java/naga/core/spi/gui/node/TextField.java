package naga.core.spi.gui.node;

import naga.core.spi.gui.properties.HasTextProperty;

/**
 * @author Bruno Salmon
 */
public interface TextField<N> extends Node<N>, HasTextProperty, UserInputNode<String, N> {

}