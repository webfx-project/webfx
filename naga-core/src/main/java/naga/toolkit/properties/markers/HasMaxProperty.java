package naga.toolkit.properties.markers;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasMaxProperty {

    Property<Integer> maxProperty();
    default void setMax(Integer value) { maxProperty().setValue(value); }
    default Integer getMax() { return maxProperty().getValue(); }

}
