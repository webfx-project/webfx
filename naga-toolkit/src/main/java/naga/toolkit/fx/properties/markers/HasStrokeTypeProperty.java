package naga.toolkit.fx.properties.markers;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.shape.StrokeType;

/**
 * @author Bruno Salmon
 */
public interface HasStrokeTypeProperty {

    Property<StrokeType> strokeTypeProperty();
    default void setStrokeType(StrokeType strokeType) {
        strokeTypeProperty().setValue(strokeType);
    }
    default StrokeType getStrokeType() {
        return strokeTypeProperty().getValue();
    }
}
