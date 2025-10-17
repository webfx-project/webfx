package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasAngleProperty {

    DoubleProperty angleProperty();
    default void setAngle(double angle) { angleProperty().setValue(angle); }
    default double getAngle() { return angleProperty().getValue(); }

}
