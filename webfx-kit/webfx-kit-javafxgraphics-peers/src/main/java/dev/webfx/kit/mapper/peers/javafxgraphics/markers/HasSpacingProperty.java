package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasSpacingProperty {

    DoubleProperty spacingProperty();
    default void setSpacing(double value) { spacingProperty().setValue(value); }
    default double getSpacing() { return spacingProperty().getValue(); }

}
