package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasMaxWidthProperty {

    DoubleProperty maxWidthProperty();
    default void setMaxWidth(double maxWidth) { maxWidthProperty().setValue(maxWidth); }
    default double getMaxWidth() { return maxWidthProperty().getValue(); }

}
