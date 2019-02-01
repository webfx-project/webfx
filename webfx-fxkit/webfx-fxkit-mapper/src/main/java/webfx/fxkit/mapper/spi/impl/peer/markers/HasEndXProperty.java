package webfx.fxkit.mapper.spi.impl.peer.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasEndXProperty {

    DoubleProperty endXProperty();

    default void setEndX(Number endX) {
        endXProperty().setValue(endX);
    }

    default Double getEndX() {
        return endXProperty().getValue();
    }
}
