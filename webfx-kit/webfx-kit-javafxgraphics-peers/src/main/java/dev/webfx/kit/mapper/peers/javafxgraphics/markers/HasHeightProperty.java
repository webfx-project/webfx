package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasHeightProperty extends HasReadOnlyHeightProperty {

    DoubleProperty heightProperty();
    default void setHeight(double height) { heightProperty().setValue(height); }

}
