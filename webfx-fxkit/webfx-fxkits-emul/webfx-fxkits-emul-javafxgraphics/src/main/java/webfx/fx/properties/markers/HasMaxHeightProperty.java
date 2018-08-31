package webfx.fx.properties.markers;

import emul.javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasMaxHeightProperty {

    Property<Double> maxHeightProperty();
    default void setMaxHeight(Double maxHeight) { maxHeightProperty().setValue(maxHeight); }
    default Double getMaxHeight() { return maxHeightProperty().getValue(); }

}
