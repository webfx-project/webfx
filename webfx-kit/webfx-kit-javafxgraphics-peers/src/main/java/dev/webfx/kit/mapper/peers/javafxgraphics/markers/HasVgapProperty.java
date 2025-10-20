package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasVgapProperty {

    DoubleProperty vgapProperty();
    default void setVgap(double vgap) { vgapProperty().setValue(vgap); }
    default double getVgap() { return vgapProperty().getValue(); }

}
