package webfx.fxkits.core.properties.markers;

import emul.javafx.beans.property.Property;

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
