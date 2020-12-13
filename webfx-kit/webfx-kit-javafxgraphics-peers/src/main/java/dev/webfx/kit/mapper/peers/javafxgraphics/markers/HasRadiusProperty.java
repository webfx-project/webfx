package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasRadiusProperty {

    DoubleProperty radiusProperty();

    default void setRadius(Number height) {
        radiusProperty().setValue(height);
    }

    default Double getRadius() {
        return radiusProperty().getValue();
    }

}
