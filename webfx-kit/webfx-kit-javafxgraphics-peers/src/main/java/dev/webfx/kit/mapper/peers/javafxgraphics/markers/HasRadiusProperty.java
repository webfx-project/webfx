package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasRadiusProperty {

    DoubleProperty radiusProperty();

    default void setRadius(double height) {
        radiusProperty().setValue(height);
    }

    default double getRadius() {
        return radiusProperty().getValue();
    }

}
