package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasPrefHeightProperty {

    DoubleProperty prefHeightProperty();
    default void setPrefHeight(double prefHeight) { prefHeightProperty().setValue(prefHeight); }
    default double getPrefHeight() { return prefHeightProperty().getValue(); }

}
