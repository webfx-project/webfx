package webfx.fxkits.core.properties.markers;

import emul.javafx.beans.property.Property;
import emul.javafx.scene.shape.StrokeLineCap;

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
