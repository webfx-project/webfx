package naga.fx.properties.markers;

import emul.javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasCenterYProperty {

    Property<Double> centerYProperty();

    default void setCenterY(Double centerY) {
        centerYProperty().setValue(centerY);
    }

    default Double getCenterY() {
        return centerYProperty().getValue();
    }

}
