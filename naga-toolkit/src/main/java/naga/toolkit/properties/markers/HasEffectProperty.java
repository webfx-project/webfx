package naga.toolkit.properties.markers;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.effect.Effect;

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
