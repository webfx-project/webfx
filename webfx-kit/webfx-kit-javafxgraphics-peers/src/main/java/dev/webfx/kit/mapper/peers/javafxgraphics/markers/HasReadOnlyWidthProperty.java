package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.ReadOnlyDoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasReadOnlyWidthProperty {

    ReadOnlyDoubleProperty widthProperty();
    default double getWidth() { return widthProperty().getValue(); }

}
