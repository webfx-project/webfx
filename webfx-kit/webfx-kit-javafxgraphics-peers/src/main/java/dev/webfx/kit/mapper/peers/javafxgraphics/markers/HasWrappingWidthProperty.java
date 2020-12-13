package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasWrappingWidthProperty {

    DoubleProperty wrappingWidthProperty();
    default void setWrappingWidth(Number wrappingWidth) { wrappingWidthProperty().setValue(wrappingWidth); }
    default Double getWrappingWidth() { return wrappingWidthProperty().getValue(); }

}
