package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.ReadOnlyDoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasReadOnlyHeightProperty {

    ReadOnlyDoubleProperty heightProperty();
    default double getHeight() { return heightProperty().getValue(); }

}
