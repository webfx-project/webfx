package dev.webfx.kit.mapper.peers.javafxgraphics.markers;


import javafx.beans.property.BooleanProperty;

/**
 * @author Bruno Salmon
 */
public interface HasMouseTransparentProperty {

    BooleanProperty mouseTransparentProperty();
    default void setMouseTransparent(boolean mouseTransparent) { mouseTransparentProperty().setValue(mouseTransparent); }
    default boolean isMouseTransparent() { return mouseTransparentProperty().getValue(); }

}
