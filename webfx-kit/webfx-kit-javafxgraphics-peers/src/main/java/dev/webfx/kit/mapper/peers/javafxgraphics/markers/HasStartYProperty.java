package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasStartYProperty {

    DoubleProperty startYProperty();

    default void setStartY(double startY) {
        startYProperty().setValue(startY);
    }

    default double getStartY() {
        return startYProperty().getValue();
    }
}
