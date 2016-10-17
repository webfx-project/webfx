package naga.toolkit.properties.markers;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasCenterXProperty {

    Property<Double> centerXProperty();

    default void setCenterX(Double centerX) {
        centerXProperty().setValue(centerX);
    }

    default Double getCenterX() {
        return centerXProperty().getValue();
    }

}
