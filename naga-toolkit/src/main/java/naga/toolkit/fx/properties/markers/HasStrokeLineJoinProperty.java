package naga.toolkit.fx.properties.markers;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.shape.StrokeLineJoin;

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
