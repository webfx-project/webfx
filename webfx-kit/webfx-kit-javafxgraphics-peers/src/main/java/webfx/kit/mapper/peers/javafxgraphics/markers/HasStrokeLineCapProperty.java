package webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.Property;
import javafx.scene.shape.StrokeLineCap;

/**
 * @author Bruno Salmon
 */
public interface HasStrokeLineCapProperty {

    Property<StrokeLineCap> strokeLineCapProperty();
    default void setStrokeLineCap(StrokeLineCap strokeLineCap) {
        strokeLineCapProperty().setValue(strokeLineCap);
    }
    default StrokeLineCap getStrokeLineCap() {
        return strokeLineCapProperty().getValue();
    }
}
