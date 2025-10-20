package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.ObjectProperty;
import javafx.scene.effect.BlendMode;

/**
 * @author Bruno Salmon
 */
public interface HasBlendModeProperty {

    ObjectProperty<BlendMode> blendModeProperty();
    default void setBlendMode(BlendMode blendMode) {
        blendModeProperty().setValue(blendMode);
    }
    default BlendMode getBlendMode() {
        return blendModeProperty().getValue();
    }

}
