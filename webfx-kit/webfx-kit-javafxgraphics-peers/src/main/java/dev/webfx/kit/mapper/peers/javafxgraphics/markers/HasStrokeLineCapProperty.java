package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.ObjectProperty;
import javafx.scene.shape.StrokeLineCap;

/**
 * @author Bruno Salmon
 */
public interface HasStrokeLineCapProperty {

    ObjectProperty<StrokeLineCap> strokeLineCapProperty();
    default void setStrokeLineCap(StrokeLineCap strokeLineCap) {
        strokeLineCapProperty().setValue(strokeLineCap);
    }
    default StrokeLineCap getStrokeLineCap() {
        return strokeLineCapProperty().getValue();
    }
}
