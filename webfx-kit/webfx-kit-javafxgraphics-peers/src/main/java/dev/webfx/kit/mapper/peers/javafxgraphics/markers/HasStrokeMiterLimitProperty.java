package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasStrokeMiterLimitProperty {

    DoubleProperty strokeMiterLimitProperty();
    default void setStrokeMiterLimit(double strokeMiterLimit) { strokeMiterLimitProperty().setValue(strokeMiterLimit); }
    default double getStrokeMiterLimit() { return strokeMiterLimitProperty().getValue(); }

}
