package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasXProperty {

    DoubleProperty xProperty();

    default void setX(double x) {
        xProperty().setValue(x);
    }

    default double getX() {
        return xProperty().getValue();
    }
}
