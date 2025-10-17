package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasMaxProperty {

    DoubleProperty maxProperty();
    default void setMax(double value) { maxProperty().setValue(value); }
    default double getMax() { return maxProperty().getValue(); }

}
