package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasWrappingWidthProperty {

    DoubleProperty wrappingWidthProperty();
    default void setWrappingWidth(double wrappingWidth) { wrappingWidthProperty().setValue(wrappingWidth); }
    default double getWrappingWidth() { return wrappingWidthProperty().getValue(); }

}
