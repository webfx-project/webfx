package webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasStrokeDashOffsetProperty {

    DoubleProperty strokeDashOffsetProperty();
    default void setStrokeDashOffset(Number strokeDashOffset) { strokeDashOffsetProperty().setValue(strokeDashOffset); }
    default Double getStrokeDashOffset() { return strokeDashOffsetProperty().getValue(); }

}
