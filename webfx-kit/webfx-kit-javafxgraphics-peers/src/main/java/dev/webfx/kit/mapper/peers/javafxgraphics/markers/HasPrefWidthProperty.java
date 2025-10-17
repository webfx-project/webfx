package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasPrefWidthProperty {

    DoubleProperty prefWidthProperty();
    default void setPrefWidth(double prefWidth) { prefWidthProperty().setValue(prefWidth); }
    default double getPrefWidth() { return prefWidthProperty().getValue(); }

}
