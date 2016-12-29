package naga.fx.properties.markers;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasMaxProperty {

    Property<Double> maxProperty();
    default void setMax(Double value) { maxProperty().setValue(value); }
    default Double getMax() { return maxProperty().getValue(); }

}
