package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasLayoutYProperty {

    DoubleProperty layoutYProperty();
    default void setLayoutY(double layoutY) { layoutYProperty().setValue(layoutY); }
    default double getLayoutY() { return layoutYProperty().getValue(); }

}
