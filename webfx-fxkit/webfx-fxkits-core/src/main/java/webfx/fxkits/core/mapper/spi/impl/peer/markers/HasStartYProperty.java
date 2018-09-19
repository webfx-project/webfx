package webfx.fxkits.core.mapper.spi.impl.peer.markers;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasStartYProperty {

    Property<Double> startYProperty();

    default void setStartY(Double startY) {
        startYProperty().setValue(startY);
    }

    default Double getStartY() {
        return startYProperty().getValue();
    }
}
