package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasMinHeightProperty {

    DoubleProperty minHeightProperty();
    default void setMinHeight(Number minHeight) { minHeightProperty().setValue(minHeight); }
    default Double getMinHeight() { return minHeightProperty().getValue(); }

}
