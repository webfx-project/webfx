package dev.webfx.kit.mapper.peers.javafxgraphics.markers;


import javafx.beans.property.BooleanProperty;

/**
 * @author Bruno Salmon
 */
public interface HasFillWidthProperty {

    BooleanProperty fillWidthProperty();
    default void setFillWidth(boolean fillWidth) { fillWidthProperty().setValue(fillWidth); }
    default boolean isFillWidth() { return fillWidthProperty().getValue(); }

}
