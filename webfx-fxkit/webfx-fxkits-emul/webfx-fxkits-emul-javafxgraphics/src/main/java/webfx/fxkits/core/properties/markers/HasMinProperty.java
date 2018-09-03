package webfx.fxkits.core.properties.markers;

import emul.javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasMinProperty {

    DoubleProperty minProperty();
    default void setMin(Number value) { minProperty().setValue(value); }
    default Double getMin() { return minProperty().getValue(); }

}
