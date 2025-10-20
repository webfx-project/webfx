package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasFitHeightProperty {

    DoubleProperty fitHeightProperty();
    default void setFitHeight(double fitHeight) { fitHeightProperty().setValue(fitHeight); }
    default double getFitHeight() { return fitHeightProperty().getValue(); }

}
