package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.IntegerProperty;

/**
 * @author Bruno Salmon
 */
public interface HasLineClampProperty {

    IntegerProperty lineClampProperty();
    default void setLineClamp(Number height) { lineClampProperty().setValue(height); }
    default int getLineClamp() { return lineClampProperty().getValue(); }

}
