package dev.webfx.kit.mapper.peers.javafxgraphics.markers;


import javafx.beans.property.BooleanProperty;

/**
 * @author Bruno Salmon
 */
public interface HasSelectedProperty {

    BooleanProperty selectedProperty();
    default void setSelected(boolean selected) { selectedProperty().setValue(selected); }
    default boolean isSelected() { return selectedProperty().getValue(); }

}
