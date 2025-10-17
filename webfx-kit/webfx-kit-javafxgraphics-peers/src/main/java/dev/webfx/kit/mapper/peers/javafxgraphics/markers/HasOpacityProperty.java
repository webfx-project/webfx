package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasOpacityProperty {

    DoubleProperty opacityProperty();
    default void setOpacity(double opacity) { opacityProperty().setValue(opacity); }
    default double getOpacity() { return opacityProperty().getValue(); }

}
