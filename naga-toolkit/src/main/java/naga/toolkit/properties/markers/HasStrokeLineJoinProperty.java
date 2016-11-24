package naga.toolkit.properties.markers;

import javafx.beans.property.Property;
import naga.toolkit.drawing.shape.StrokeLineJoin;

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
