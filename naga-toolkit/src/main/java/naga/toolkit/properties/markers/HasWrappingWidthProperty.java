package naga.toolkit.properties.markers;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasWrappingWidthProperty {

    Property<Double> wrappingWidthProperty();
    default void setWrappingWidth(Double wrappingWidth) { wrappingWidthProperty().setValue(wrappingWidth); }
    default Double getWrappingWidth() { return wrappingWidthProperty().getValue(); }

}
