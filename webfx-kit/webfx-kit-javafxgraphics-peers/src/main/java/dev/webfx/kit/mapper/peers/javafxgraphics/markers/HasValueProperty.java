package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasValueProperty {

    DoubleProperty valueProperty();
    default void setValue(double value) { valueProperty().setValue(value); }
    default double getValue() { return valueProperty().getValue(); }

}
