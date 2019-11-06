package webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasPivotYProperty {

    DoubleProperty pivotYProperty();
    default void setPivotY(Number pivotY) { pivotYProperty().setValue(pivotY); }
    default Double getPivotY() { return pivotYProperty().getValue(); }

}
