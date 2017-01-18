package naga.fx.properties.markers;

import emul.javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasArcWidthProperty {

    Property<Double> arcWidthProperty();
    default void setArcWidth(Double arcWidth) { arcWidthProperty().setValue(arcWidth); }
    default Double getArcWidth() { return arcWidthProperty().getValue(); }

}
