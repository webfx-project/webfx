package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasHgapProperty {

    DoubleProperty hgapProperty();
    default void setHgap(double hgap) { hgapProperty().setValue(hgap); }
    default double getHgap() { return hgapProperty().getValue(); }

}
