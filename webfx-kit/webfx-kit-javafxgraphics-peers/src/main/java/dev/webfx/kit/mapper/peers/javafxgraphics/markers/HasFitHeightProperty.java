package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasFitHeightProperty {

    DoubleProperty fitHeightProperty();
    default void setFitHeight(Number fitHeight) { fitHeightProperty().setValue(fitHeight); }
    default Double getFitHeight() { return fitHeightProperty().getValue(); }

}
