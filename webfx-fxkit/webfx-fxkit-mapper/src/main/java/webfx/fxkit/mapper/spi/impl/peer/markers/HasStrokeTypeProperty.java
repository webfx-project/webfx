package webfx.fxkit.mapper.spi.impl.peer.markers;

import javafx.beans.property.Property;
import javafx.scene.shape.StrokeType;

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
