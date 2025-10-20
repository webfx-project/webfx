package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.ObjectProperty;
import javafx.scene.Parent;

/**
 * @author Bruno Salmon
 */
public interface HasParentProperty {

    ObjectProperty<Parent> parentProperty();
    default void setParent(Parent parent) {
        parentProperty().setValue(parent);
    }
    default Parent getParent() {
        return parentProperty().getValue();
    }

}
