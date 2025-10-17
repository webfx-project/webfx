package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.VPos;

/**
 * @author Bruno Salmon
 */
public interface HasTextOriginProperty {

    ObjectProperty<VPos> textOriginProperty();
    default void setTextOrigin(VPos textOrigin) {
        textOriginProperty().setValue(textOrigin);
    }
    default VPos getTextOrigin() {
        return textOriginProperty().getValue();
    }

}
