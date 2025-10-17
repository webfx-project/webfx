package dev.webfx.kit.mapper.peers.javafxgraphics.markers;


import javafx.beans.property.BooleanProperty;

/**
 * @author Bruno Salmon
 */
public interface HasSmoothProperty {

    BooleanProperty smoothProperty();
    default void setSmooth(boolean smooth) { smoothProperty().setValue(smooth); }
    default boolean isSmooth() { return smoothProperty().getValue(); }

}
