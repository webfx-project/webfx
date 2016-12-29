package naga.fx.properties.markers;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasSpacingProperty {

    Property<Double> spacingProperty();
    default void setSpacing(Double value) { spacingProperty().setValue(value); }
    default Double getSpacing() { return spacingProperty().getValue(); }

}
