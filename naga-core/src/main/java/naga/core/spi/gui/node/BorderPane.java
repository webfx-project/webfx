package naga.core.spi.gui.node;


import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface BorderPane<BP, N> extends Node<BP> {

    Property<Node<N>> topProperty();
    default void setTop(Node<N> node) { topProperty().setValue(node); }
    default Node<N> getTop() { return topProperty().getValue(); }

    Property<Node<N>> centerProperty();
    default void setCenter(Node<N> node) { centerProperty().setValue(node); }
    default Node<N> getCenter() { return centerProperty().getValue(); }

    Property<Node<N>> bottomProperty();
    default void setBottom(Node<N> node) { bottomProperty().setValue(node); }
    default Node<N> getBottom() { return bottomProperty().getValue(); }

}
