package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasYProperty {

    DoubleProperty yProperty();

    default void setY(Number y) {
        yProperty().setValue(y);
    }

    default Double getY() {
        return yProperty().getValue();
    }
}
