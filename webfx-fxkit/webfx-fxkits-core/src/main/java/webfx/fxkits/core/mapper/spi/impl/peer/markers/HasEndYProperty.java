package webfx.fxkits.core.mapper.spi.impl.peer.markers;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasEndYProperty {

    Property<Double> endYProperty();

    default void setEndY(Double endY) {
        endYProperty().setValue(endY);
    }

    default Double getEndY() {
        return endYProperty().getValue();
    }
}
