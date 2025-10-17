package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasEndYProperty {

    DoubleProperty endYProperty();

    default void setEndY(double endY) {
        endYProperty().setValue(endY);
    }

    default double getEndY() {
        return endYProperty().getValue();
    }
}
