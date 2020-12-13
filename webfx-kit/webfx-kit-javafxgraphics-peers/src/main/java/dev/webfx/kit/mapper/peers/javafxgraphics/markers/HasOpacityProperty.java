package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasOpacityProperty {

    DoubleProperty opacityProperty();
    default void setOpacity(Number opacity) { opacityProperty().setValue(opacity); }
    default Double getOpacity() { return opacityProperty().getValue(); }

}
