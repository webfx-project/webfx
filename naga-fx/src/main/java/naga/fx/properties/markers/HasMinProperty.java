package naga.fx.properties.markers;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasMinProperty {

    Property<Double> minProperty();
    default void setMin(Double value) { minProperty().setValue(value); }
    default Double getMin() { return minProperty().getValue(); }

}
