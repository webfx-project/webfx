package webfx.fxkits.core.properties.markers;

import javafx.beans.property.Property;
import javafx.geometry.Orientation;

/**
 * @author Bruno Salmon
 */
public interface HasOrientationProperty {

    Property<Orientation> orientationProperty();
    default void setOrientation(Orientation orientation) {
        orientationProperty().setValue(orientation);
    }
    default Orientation getOrientation() {
        return orientationProperty().getValue();
    }

}
