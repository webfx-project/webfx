package naga.toolkit.drawing.text;

import naga.toolkit.drawing.shape.Shape;
import naga.toolkit.drawing.text.impl.TextImpl;
import naga.toolkit.properties.markers.*;

/**
 * @author Bruno Salmon
 */
public interface Text extends Shape,
        HasXProperty,
        HasYProperty,
        HasTextProperty,
        HasTextOriginProperty,
        HasTextAlignmentProperty,
        HasWrappingWidthProperty,
        HasFontProperty {

    static Text create() {
        return new TextImpl();
    }

}
