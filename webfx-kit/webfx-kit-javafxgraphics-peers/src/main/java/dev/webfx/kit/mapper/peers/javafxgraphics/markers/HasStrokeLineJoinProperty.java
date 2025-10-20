package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.ObjectProperty;
import javafx.scene.shape.StrokeLineJoin;

/**
 * @author Bruno Salmon
 */
public interface HasStrokeLineJoinProperty {

    ObjectProperty<StrokeLineJoin> strokeLineJoinProperty();
    default void setStrokeLineJoin(StrokeLineJoin strokeLineJoin) {
        strokeLineJoinProperty().setValue(strokeLineJoin);
    }
    default StrokeLineJoin getStrokeLineJoin() {
        return strokeLineJoinProperty().getValue();
    }
}
