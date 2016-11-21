package naga.toolkit.drawing.shapes;

import naga.toolkit.drawing.shapes.impl.TextShapeImpl;
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
