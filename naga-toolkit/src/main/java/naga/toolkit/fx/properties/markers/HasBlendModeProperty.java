package naga.toolkit.fx.properties.markers;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.effect.BlendMode;

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
