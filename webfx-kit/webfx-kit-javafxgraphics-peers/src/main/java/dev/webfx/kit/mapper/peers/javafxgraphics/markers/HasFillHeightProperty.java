package dev.webfx.kit.mapper.peers.javafxgraphics.markers;


import javafx.beans.property.BooleanProperty;

/**
 * @author Bruno Salmon
 */
public interface HasFillHeightProperty {

    BooleanProperty fillHeightProperty();
    default void setFillHeight(boolean fillHeight) { fillHeightProperty().setValue(fillHeight); }
    default boolean isFillHeight() { return fillHeightProperty().getValue(); }

}
