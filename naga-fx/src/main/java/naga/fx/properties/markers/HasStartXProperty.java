package naga.fx.properties.markers;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasStartXProperty {

    Property<Double> startXProperty();

    default void setStartX(Double startX) {
        startXProperty().setValue(startX);
    }

    default Double getStartX() {
        return startXProperty().getValue();
    }
}
