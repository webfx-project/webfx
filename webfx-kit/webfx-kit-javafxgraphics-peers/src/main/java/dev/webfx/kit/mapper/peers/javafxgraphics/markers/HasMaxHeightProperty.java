package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasMaxHeightProperty {

    DoubleProperty maxHeightProperty();
    default void setMaxHeight(double maxHeight) { maxHeightProperty().setValue(maxHeight); }
    default double getMaxHeight() { return maxHeightProperty().getValue(); }

}
