package naga.toolkit.properties.markers;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasValueProperty {

    Property<Double> valueProperty();
    default void setValue(Double value) { valueProperty().setValue(value); }
    default Double getValue() { return valueProperty().getValue(); }

}
