package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasStartXProperty {

    DoubleProperty startXProperty();

    default void setStartX(double startX) {
        startXProperty().setValue(startX);
    }

    default double getStartX() {
        return startXProperty().getValue();
    }
}
