package naga.toolkit.properties.markers;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasMinWidthProperty {

    Property<Double> minWidthProperty();
    default void setMinWidth(Double minWidth) { minWidthProperty().setValue(minWidth); }
    default Double getMinWidth() { return minWidthProperty().getValue(); }

}
