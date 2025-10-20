package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Orientation;

/**
 * @author Bruno Salmon
 */
public interface HasOrientationProperty {

    ObjectProperty<Orientation> orientationProperty();
    default void setOrientation(Orientation orientation) {
        orientationProperty().setValue(orientation);
    }
    default Orientation getOrientation() {
        return orientationProperty().getValue();
    }

}
