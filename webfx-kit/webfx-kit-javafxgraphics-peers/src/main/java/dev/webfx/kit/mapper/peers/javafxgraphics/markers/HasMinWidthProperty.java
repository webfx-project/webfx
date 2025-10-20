package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasMinWidthProperty {

    DoubleProperty minWidthProperty();
    default void setMinWidth(double minWidth) { minWidthProperty().setValue(minWidth); }
    default double getMinWidth() { return minWidthProperty().getValue(); }

}
