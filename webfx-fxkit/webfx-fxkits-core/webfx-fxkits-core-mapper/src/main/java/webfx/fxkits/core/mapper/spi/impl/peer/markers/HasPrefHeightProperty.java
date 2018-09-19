package webfx.fxkits.core.mapper.spi.impl.peer.markers;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasPrefHeightProperty {

    Property<Double> prefHeightProperty();
    default void setPrefHeight(Double prefHeight) { prefHeightProperty().setValue(prefHeight); }
    default Double getPrefHeight() { return prefHeightProperty().getValue(); }

}
