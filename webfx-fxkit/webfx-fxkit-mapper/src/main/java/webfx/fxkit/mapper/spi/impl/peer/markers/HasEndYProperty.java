package webfx.fxkit.mapper.spi.impl.peer.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasEndYProperty {

    DoubleProperty endYProperty();

    default void setEndY(Number endY) {
        endYProperty().setValue(endY);
    }

    default Double getEndY() {
        return endYProperty().getValue();
    }
}
