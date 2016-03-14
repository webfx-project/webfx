package naga.core.spi.gui.node;


import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface UserInputNode<I, N> extends Node<N> {

    Property<I> getUserInputProperty();

}
