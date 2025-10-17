package dev.webfx.kit.mapper.peers.javafxgraphics.markers;


import javafx.beans.property.BooleanProperty;

/**
 * @author Bruno Salmon
 */
public interface HasSnapToPixelProperty {

    BooleanProperty snapToPixelProperty();
    default void setSnapToPixel(boolean snapToPixel) { snapToPixelProperty().setValue(snapToPixel); }
    default boolean isSnapToPixel() { return snapToPixelProperty().getValue(); }

}
