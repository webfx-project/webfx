package naga.toolkit.fx.properties.markers;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasXProperty {

    Property<Double> xProperty();

    default void setX(Double x) {
        xProperty().setValue(x);
    }

    default Double getX() {
        return xProperty().getValue();
    }
}
