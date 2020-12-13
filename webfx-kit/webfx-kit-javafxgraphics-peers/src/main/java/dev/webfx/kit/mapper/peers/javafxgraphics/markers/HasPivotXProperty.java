package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasPivotXProperty {

    DoubleProperty pivotXProperty();
    default void setPivotX(Number pivotX) { pivotXProperty().setValue(pivotX); }
    default Double getPivotX() { return pivotXProperty().getValue(); }

}
