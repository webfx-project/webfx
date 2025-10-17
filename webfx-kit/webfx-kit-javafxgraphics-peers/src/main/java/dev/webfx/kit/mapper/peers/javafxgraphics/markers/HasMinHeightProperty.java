package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasMinHeightProperty {

    DoubleProperty minHeightProperty();
    default void setMinHeight(double minHeight) { minHeightProperty().setValue(minHeight); }
    default double getMinHeight() { return minHeightProperty().getValue(); }

}
