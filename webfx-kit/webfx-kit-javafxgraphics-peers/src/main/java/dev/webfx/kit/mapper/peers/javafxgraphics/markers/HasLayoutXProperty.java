package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasLayoutXProperty {

    DoubleProperty layoutXProperty();
    default void setLayoutX(Number layoutX) { layoutXProperty().setValue(layoutX); }
    default Double getLayoutX() { return layoutXProperty().getValue(); }

}
