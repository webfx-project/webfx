package naga.fx.properties.markers;

import emul.javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasArcHeightProperty {

    Property<Double> arcHeightProperty();
    default void setArcHeight(Double arcHeight) { arcHeightProperty().setValue(arcHeight); }
    default Double getArcHeight() { return arcHeightProperty().getValue(); }

}
