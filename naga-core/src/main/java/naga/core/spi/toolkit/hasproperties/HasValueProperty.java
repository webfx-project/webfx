package naga.core.spi.toolkit.hasproperties;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasValueProperty {

    Property<Integer> valueProperty();
    default void setValue(Integer value) { valueProperty().setValue(value); }
    default Integer getValue() { return valueProperty().getValue(); }

}
