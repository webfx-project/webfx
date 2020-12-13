package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasStartYProperty {

    DoubleProperty startYProperty();

    default void setStartY(Number startY) {
        startYProperty().setValue(startY);
    }

    default Double getStartY() {
        return startYProperty().getValue();
    }
}
