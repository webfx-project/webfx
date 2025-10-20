package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;

/**
 * @author Bruno Salmon
 */
public interface HasPaddingProperty {

    ObjectProperty<Insets> paddingProperty();
    default void setPadding(Insets insets) {
        paddingProperty().setValue(insets);
    }
    default Insets getPadding() {
        return paddingProperty().getValue();
    }

}
