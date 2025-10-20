package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasCenterYProperty {

    DoubleProperty centerYProperty();

    default void setCenterY(double centerY) {
        centerYProperty().setValue(centerY);
    }

    default double getCenterY() {
        return centerYProperty().getValue();
    }

}
