package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasMinProperty {

    DoubleProperty minProperty();
    default void setMin(double value) { minProperty().setValue(value); }
    default double getMin() { return minProperty().getValue(); }

}
