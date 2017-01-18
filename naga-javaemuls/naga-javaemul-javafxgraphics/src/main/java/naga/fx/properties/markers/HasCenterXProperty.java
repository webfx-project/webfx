package naga.fx.properties.markers;

import emul.javafx.beans.property.Property;

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
