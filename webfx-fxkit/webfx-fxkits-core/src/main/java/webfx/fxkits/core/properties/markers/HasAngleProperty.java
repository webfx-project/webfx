package webfx.fxkits.core.properties.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasAngleProperty {

    DoubleProperty angleProperty();
    default void setAngle(Number angle) { angleProperty().setValue(angle); }
    default Double getAngle() { return angleProperty().getValue(); }

}
