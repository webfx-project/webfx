package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasPivotYProperty {

    DoubleProperty pivotYProperty();
    default void setPivotY(double pivotY) { pivotYProperty().setValue(pivotY); }
    default double getPivotY() { return pivotYProperty().getValue(); }

}
