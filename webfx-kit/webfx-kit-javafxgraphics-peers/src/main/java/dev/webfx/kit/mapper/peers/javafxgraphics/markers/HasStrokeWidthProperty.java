package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasStrokeWidthProperty {

    DoubleProperty strokeWidthProperty();
    default void setStrokeWidth(double strokeWidth) { strokeWidthProperty().setValue(strokeWidth); }
    default double getStrokeWidth() { return strokeWidthProperty().getValue(); }

}
