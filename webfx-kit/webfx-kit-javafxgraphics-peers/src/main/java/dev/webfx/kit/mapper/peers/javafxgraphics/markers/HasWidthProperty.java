package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasWidthProperty extends HasReadOnlyWidthProperty {

    DoubleProperty widthProperty();
    default void setWidth(double width) { widthProperty().setValue(width); }

}
