package dev.webfx.kit.mapper.peers.javafxgraphics.markers;


import javafx.beans.property.BooleanProperty;

/**
 * @author Bruno Salmon
 */
public interface HasAutoSizeChildrenProperty {

    BooleanProperty autoSizeChildrenProperty();
    default void setAutoSizeChildren(boolean autoSizeChildren) { autoSizeChildrenProperty().setValue(autoSizeChildren); }
    default boolean isAutoSizeChildren() { return autoSizeChildrenProperty().getValue(); }

}
