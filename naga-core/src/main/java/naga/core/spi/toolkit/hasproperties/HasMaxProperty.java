package naga.core.spi.toolkit.hasproperties;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasMaxProperty {

    Property<Integer> maxProperty();
    default void setMax(Integer value) { maxProperty().setValue(value); }
    default Integer getMax() { return maxProperty().getValue(); }

}
