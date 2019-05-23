package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasStartXProperty {

    DoubleProperty startXProperty();

    default void setStartX(Number startX) {
        startXProperty().setValue(startX);
    }

    default Double getStartX() {
        return startXProperty().getValue();
    }
}
