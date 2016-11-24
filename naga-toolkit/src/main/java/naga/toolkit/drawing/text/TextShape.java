package naga.toolkit.drawing.text;

import naga.toolkit.drawing.shape.Shape;
import naga.toolkit.drawing.text.impl.TextShapeImpl;
import naga.toolkit.properties.markers.*;

/**
 * @author Bruno Salmon
 */
public interface TextShape extends Shape,
        HasXProperty,
        HasYProperty,
        HasTextProperty,
        HasTextOriginProperty,
        HasTextAlignmentProperty,
        HasWrappingWidthProperty,
        HasFontProperty {

    static TextShape create() {
        return new TextShapeImpl();
    }

}
