package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasWidthProperty {

    DoubleProperty widthProperty();
    default void setWidth(Number width) { widthProperty().setValue(width); }
    default double getWidth() { return widthProperty().getValue(); }

}
