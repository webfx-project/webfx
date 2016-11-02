package naga.toolkit.properties.markers;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasAngleProperty {

    Property<Double> angleProperty();
    default void setAngle(Double angle) { angleProperty().setValue(angle); }
    default Double getAngle() { return angleProperty().getValue(); }

}
