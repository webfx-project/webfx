package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasPrefHeightProperty {

    DoubleProperty prefHeightProperty();
    default void setPrefHeight(Number prefHeight) { prefHeightProperty().setValue(prefHeight); }
    default Double getPrefHeight() { return prefHeightProperty().getValue(); }

}
