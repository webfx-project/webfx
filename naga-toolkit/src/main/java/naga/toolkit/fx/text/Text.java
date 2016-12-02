package naga.toolkit.fx.text;

import naga.toolkit.fx.shape.Shape;
import naga.toolkit.fx.text.impl.TextImpl;
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
