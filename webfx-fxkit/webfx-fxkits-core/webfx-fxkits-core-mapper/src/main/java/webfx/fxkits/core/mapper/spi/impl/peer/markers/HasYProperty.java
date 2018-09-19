package webfx.fxkits.core.mapper.spi.impl.peer.markers;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasYProperty {

    Property<Double> yProperty();

    default void setY(Double y) {
        yProperty().setValue(y);
    }

    default Double getY() {
        return yProperty().getValue();
    }
}
