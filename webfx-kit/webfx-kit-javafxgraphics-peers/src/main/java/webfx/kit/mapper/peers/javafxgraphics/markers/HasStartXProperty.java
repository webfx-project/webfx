package webfx.kit.mapper.peers.javafxgraphics.markers;

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
