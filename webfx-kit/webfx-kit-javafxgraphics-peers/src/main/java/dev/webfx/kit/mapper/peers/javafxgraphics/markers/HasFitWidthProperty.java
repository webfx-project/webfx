package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasFitWidthProperty {

    DoubleProperty fitWidthProperty();
    default void setFitWidth(double fitWidth) { fitWidthProperty().setValue(fitWidth); }
    default double getFitWidth() { return fitWidthProperty().getValue(); }

}
