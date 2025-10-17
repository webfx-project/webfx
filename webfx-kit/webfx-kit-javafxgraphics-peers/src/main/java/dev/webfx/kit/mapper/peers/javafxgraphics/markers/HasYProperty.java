package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasYProperty {

    DoubleProperty yProperty();

    default void setY(double y) {
        yProperty().setValue(y);
    }

    default double getY() {
        return yProperty().getValue();
    }
}
