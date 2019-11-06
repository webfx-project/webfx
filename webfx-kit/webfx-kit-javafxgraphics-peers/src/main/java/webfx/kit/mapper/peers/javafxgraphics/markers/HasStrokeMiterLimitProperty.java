package webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasStrokeMiterLimitProperty {

    DoubleProperty strokeMiterLimitProperty();
    default void setStrokeMiterLimit(Number strokeMiterLimit) { strokeMiterLimitProperty().setValue(strokeMiterLimit); }
    default Double getStrokeMiterLimit() { return strokeMiterLimitProperty().getValue(); }

}
