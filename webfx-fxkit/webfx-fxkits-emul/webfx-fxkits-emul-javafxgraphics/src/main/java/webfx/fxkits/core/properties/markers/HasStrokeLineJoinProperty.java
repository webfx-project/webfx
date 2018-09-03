package webfx.fxkits.core.properties.markers;

import emul.javafx.beans.property.Property;
import emul.javafx.scene.shape.StrokeLineJoin;

/**
 * @author Bruno Salmon
 */
public interface HasStrokeLineJoinProperty {

    Property<StrokeLineJoin> strokeLineJoinProperty();
    default void setStrokeLineJoin(StrokeLineJoin strokeLineJoin) {
        strokeLineJoinProperty().setValue(strokeLineJoin);
    }
    default StrokeLineJoin getStrokeLineJoin() {
        return strokeLineJoinProperty().getValue();
    }
}
