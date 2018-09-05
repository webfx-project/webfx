package webfx.fxkits.core.properties.markers;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasArcHeightProperty {

    Property<Double> arcHeightProperty();
    default void setArcHeight(Double arcHeight) { arcHeightProperty().setValue(arcHeight); }
    default Double getArcHeight() { return arcHeightProperty().getValue(); }

}
