package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasPrefWrapLengthProperty {

    DoubleProperty prefWrapLengthProperty();
    default void setPrefWrapLength(double prefWrapLength) { prefWrapLengthProperty().setValue(prefWrapLength); }
    default double getPrefWrapLength() { return prefWrapLengthProperty().getValue(); }

}
