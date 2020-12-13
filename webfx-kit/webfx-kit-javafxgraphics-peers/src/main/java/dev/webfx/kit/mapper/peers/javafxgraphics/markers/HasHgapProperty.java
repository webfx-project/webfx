package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasHgapProperty {

    DoubleProperty hgapProperty();
    default void setHgap(Number hgap) { hgapProperty().setValue(hgap); }
    default Double getHgap() { return hgapProperty().getValue(); }

}
