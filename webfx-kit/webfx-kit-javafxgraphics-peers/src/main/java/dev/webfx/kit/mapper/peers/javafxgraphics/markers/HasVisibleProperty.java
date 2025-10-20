package dev.webfx.kit.mapper.peers.javafxgraphics.markers;


import javafx.beans.property.BooleanProperty;

/**
 * @author Bruno Salmon
 */
public interface HasVisibleProperty {

    BooleanProperty visibleProperty();
    default void setVisible(boolean visible) { visibleProperty().setValue(visible); }
    default boolean isVisible() { return visibleProperty().getValue(); }

}
