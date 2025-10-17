package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasLayoutXProperty {

    DoubleProperty layoutXProperty();
    default void setLayoutX(double layoutX) { layoutXProperty().setValue(layoutX); }
    default double getLayoutX() { return layoutXProperty().getValue(); }

}
