package naga.toolkit.fx.spi.view.base;

import naga.toolkit.fx.scene.text.Font;
import naga.toolkit.fx.scene.text.TextAlignment;
import naga.toolkit.fx.geometry.VPos;

/**
 * @author Bruno Salmon
 */
public interface TextViewMixin2 extends TextViewMixin {

    default void updateText(String text) {}

    default void updateTextOrigin(VPos textOrigin) {}

    default void updateX(Double x) {}

    default void updateY(Double y) {}

    default void updateWrappingWidth(Double wrappingWidth) {}

    default void updateTextAlignment(TextAlignment textAlignment) {}

    default void updateFont(Font font) {}

}
