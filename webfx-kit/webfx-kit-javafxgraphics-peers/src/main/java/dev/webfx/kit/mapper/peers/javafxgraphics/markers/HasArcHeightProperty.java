package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasArcHeightProperty {

    DoubleProperty arcHeightProperty();
    default void setArcHeight(double arcHeight) { arcHeightProperty().setValue(arcHeight); }
    default double getArcHeight() { return arcHeightProperty().getValue(); }

}
