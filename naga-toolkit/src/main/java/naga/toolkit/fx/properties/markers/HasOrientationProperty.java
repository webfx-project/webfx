package naga.toolkit.fx.properties.markers;

import javafx.beans.property.Property;
import naga.toolkit.fx.geometry.Orientation;

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
