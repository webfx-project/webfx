package webfx.fxkit.mapper.spi.impl.peer.markers;

import javafx.beans.property.Property;
import javafx.scene.shape.StrokeLineJoin;

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
