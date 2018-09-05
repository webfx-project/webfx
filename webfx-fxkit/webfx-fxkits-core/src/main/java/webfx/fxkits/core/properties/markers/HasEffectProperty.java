package webfx.fxkits.core.properties.markers;

import javafx.beans.property.Property;
import javafx.scene.effect.Effect;

/**
 * @author Bruno Salmon
 */
public interface HasEffectProperty {

    Property<Effect> effectProperty();
    default void setEffect(Effect effect) {
        effectProperty().setValue(effect);
    }
    default Effect getEffect() {
        return effectProperty().getValue();
    }
    
}
