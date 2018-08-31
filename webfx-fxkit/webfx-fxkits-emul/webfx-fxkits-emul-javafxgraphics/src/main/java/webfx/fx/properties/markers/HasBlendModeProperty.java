package webfx.fx.properties.markers;

import emul.javafx.beans.property.Property;
import emul.javafx.scene.effect.BlendMode;

/**
 * @author Bruno Salmon
 */
public interface HasBlendModeProperty {

    Property<BlendMode> blendModeProperty();
    default void setBlendMode(BlendMode blendMode) {
        blendModeProperty().setValue(blendMode);
    }
    default BlendMode getBlendMode() {
        return blendModeProperty().getValue();
    }

}
