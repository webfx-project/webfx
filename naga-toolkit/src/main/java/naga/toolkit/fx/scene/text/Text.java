package naga.toolkit.fx.scene.text;

import naga.toolkit.fx.scene.shape.Shape;
import naga.toolkit.fx.scene.text.impl.TextImpl;
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
