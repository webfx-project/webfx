package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasStrokeDashOffsetProperty {

    DoubleProperty strokeDashOffsetProperty();
    default void setStrokeDashOffset(double strokeDashOffset) { strokeDashOffsetProperty().setValue(strokeDashOffset); }
    default double getStrokeDashOffset() { return strokeDashOffsetProperty().getValue(); }

}
