package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasCenterXProperty {

    DoubleProperty centerXProperty();

    default void setCenterX(double centerX) {
        centerXProperty().setValue(centerX);
    }

    default double getCenterX() {
        return centerXProperty().getValue();
    }

}
