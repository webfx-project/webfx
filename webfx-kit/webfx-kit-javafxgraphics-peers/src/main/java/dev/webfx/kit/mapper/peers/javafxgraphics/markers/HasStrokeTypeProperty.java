package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.ObjectProperty;
import javafx.scene.shape.StrokeType;

/**
 * @author Bruno Salmon
 */
public interface HasStrokeTypeProperty {

    ObjectProperty<StrokeType> strokeTypeProperty();
    default void setStrokeType(StrokeType strokeType) {
        strokeTypeProperty().setValue(strokeType);
    }
    default StrokeType getStrokeType() {
        return strokeTypeProperty().getValue();
    }
}
