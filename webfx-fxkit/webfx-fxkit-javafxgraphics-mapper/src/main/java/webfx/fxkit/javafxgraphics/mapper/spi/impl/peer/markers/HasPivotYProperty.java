package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasPivotYProperty {

    DoubleProperty pivotYProperty();
    default void setPivotY(Number pivotY) { pivotYProperty().setValue(pivotY); }
    default Double getPivotY() { return pivotYProperty().getValue(); }

}
