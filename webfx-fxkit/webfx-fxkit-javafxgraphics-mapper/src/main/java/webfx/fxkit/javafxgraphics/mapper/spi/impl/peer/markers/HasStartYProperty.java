package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasStartYProperty {

    DoubleProperty startYProperty();

    default void setStartY(Number startY) {
        startYProperty().setValue(startY);
    }

    default Double getStartY() {
        return startYProperty().getValue();
    }
}
