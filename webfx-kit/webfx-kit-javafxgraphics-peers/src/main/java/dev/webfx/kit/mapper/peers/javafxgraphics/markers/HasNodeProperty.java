package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;

/**
 * @author Bruno Salmon
 */
public interface HasNodeProperty {

    ObjectProperty<Node> nodeProperty();
    default HasNodeProperty setNode(Node node) { nodeProperty().setValue(node); return this; }
    default Node getNode() { return nodeProperty().getValue(); }
}
