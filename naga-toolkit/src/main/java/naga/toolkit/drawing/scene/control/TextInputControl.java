package naga.toolkit.drawing.scene.control;

import naga.toolkit.properties.markers.HasFontProperty;
import naga.toolkit.properties.markers.HasPromptTextProperty;
import naga.toolkit.properties.markers.HasTextProperty;

/**
 * @author Bruno Salmon
 */
public interface TextInputControl extends Control,
        HasFontProperty,
        HasTextProperty,
        HasPromptTextProperty {
}
