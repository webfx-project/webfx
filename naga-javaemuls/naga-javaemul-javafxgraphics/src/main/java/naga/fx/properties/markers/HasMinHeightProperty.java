package naga.fx.properties.markers;

import emul.javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasMinHeightProperty {

    Property<Double> minHeightProperty();
    default void setMinHeight(Double minHeight) { minHeightProperty().setValue(minHeight); }
    default Double getMinHeight() { return minHeightProperty().getValue(); }

}
