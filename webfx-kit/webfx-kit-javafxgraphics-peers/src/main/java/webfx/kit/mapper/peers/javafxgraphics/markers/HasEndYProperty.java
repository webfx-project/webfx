package webfx.kit.mapper.peers.javafxgraphics.markers;

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
