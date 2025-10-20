package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasPivotXProperty {

    DoubleProperty pivotXProperty();
    default void setPivotX(double pivotX) { pivotXProperty().setValue(pivotX); }
    default double getPivotX() { return pivotXProperty().getValue(); }

}
