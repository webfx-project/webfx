package naga.fx.properties.markers;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasMaxWidthProperty {

    Property<Double> maxWidthProperty();
    default void setMaxWidth(Double maxWidth) { maxWidthProperty().setValue(maxWidth); }
    default Double getMaxWidth() { return maxWidthProperty().getValue(); }

}
