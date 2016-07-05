package naga.core.spi.toolkit.propertymarkers;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasMinProperty {

    Property<Integer> minProperty();
    default void setMin(Integer value) { minProperty().setValue(value); }
    default Integer getMin() { return minProperty().getValue(); }

}
