package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;

/**
 * @author Bruno Salmon
 */
public interface HasGraphicProperty {

    ObjectProperty<Node> graphicProperty();
    default void setGraphic(Node graphic) { graphicProperty().setValue(graphic); }
    default Node getGraphic() { return graphicProperty().getValue(); }
}
