package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasEndXProperty {

    DoubleProperty endXProperty();

    default void setEndX(double endX) {
        endXProperty().setValue(endX);
    }

    default double getEndX() {
        return endXProperty().getValue();
    }
}
