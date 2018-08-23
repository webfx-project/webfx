package naga.fx.properties.markers;

import emul.javafx.beans.property.Property;
import emul.javafx.scene.shape.StrokeType;

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
