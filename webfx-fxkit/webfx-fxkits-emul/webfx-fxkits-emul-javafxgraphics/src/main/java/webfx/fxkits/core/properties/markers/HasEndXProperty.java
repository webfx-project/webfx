package webfx.fxkits.core.properties.markers;

import emul.javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasEndXProperty {

    Property<Double> endXProperty();

    default void setEndX(Double endX) {
        endXProperty().setValue(endX);
    }

    default Double getEndX() {
        return endXProperty().getValue();
    }
}
