package dev.webfx.kit.mapper.peers.javafxgraphics.markers;


import javafx.beans.property.BooleanProperty;

/**
 * @author Bruno Salmon
 */
public interface HasManagedProperty {

    BooleanProperty managedProperty();
    default void setManaged(boolean managed) { managedProperty().setValue(managed); }
    default boolean isManaged() { return managedProperty().getValue(); }

}
