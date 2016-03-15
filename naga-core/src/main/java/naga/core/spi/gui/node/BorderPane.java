package naga.core.spi.gui.node;


import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface BorderPane<BP, N> extends Node<BP> {

    Property<Node<N>> topProperty();
    default BorderPane setTop(Node node) { topProperty().setValue(node); return this;}
    default Node<N> getTop() { return topProperty().getValue(); }

    Property<Node<N>> centerProperty();
    default BorderPane setCenter(Node node) { centerProperty().setValue(node); return this; }
    default Node<N> getCenter() { return centerProperty().getValue(); }

    Property<Node<N>> bottomProperty();
    default BorderPane setBottom(Node node) { bottomProperty().setValue(node); return this; }
    default Node<N> getBottom() { return bottomProperty().getValue(); }

}
