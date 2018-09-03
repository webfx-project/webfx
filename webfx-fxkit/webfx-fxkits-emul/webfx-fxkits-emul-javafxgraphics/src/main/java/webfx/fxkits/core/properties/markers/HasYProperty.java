package webfx.fxkits.core.properties.markers;

import emul.javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasYProperty {

    Property<Double> yProperty();

    default void setY(Double y) {
        yProperty().setValue(y);
    }

    default Double getY() {
        return yProperty().getValue();
    }
}
