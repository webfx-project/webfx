package naga.toolkit.drawing.shapes;

import naga.toolkit.properties.markers.HasFontProperty;
import naga.toolkit.properties.markers.HasTextProperty;
import naga.toolkit.properties.markers.HasXProperty;
import naga.toolkit.properties.markers.HasYProperty;

/**
 * @author Bruno Salmon
 */
public interface TextShape extends Shape,
        HasXProperty,
        HasYProperty,
        HasTextProperty,
        HasFontProperty {
}
