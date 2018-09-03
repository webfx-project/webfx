package webfx.fxkits.core.properties.markers;

import emul.javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasValueProperty {

    DoubleProperty valueProperty();
    default void setValue(Number value) { valueProperty().setValue(value); }
    default Double getValue() { return valueProperty().getValue(); }

}
