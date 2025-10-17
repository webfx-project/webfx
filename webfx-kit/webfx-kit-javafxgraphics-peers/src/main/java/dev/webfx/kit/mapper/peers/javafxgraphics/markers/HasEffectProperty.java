package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.ObjectProperty;
import javafx.scene.effect.Effect;

/**
 * @author Bruno Salmon
 */
public interface HasEffectProperty {

    ObjectProperty<Effect> effectProperty();
    default void setEffect(Effect effect) {
        effectProperty().setValue(effect);
    }
    default Effect getEffect() {
        return effectProperty().getValue();
    }
    
}
