package naga.toolkit.drawing.spi.view.base;

import naga.toolkit.drawing.text.Font;
import naga.toolkit.drawing.text.TextAlignment;
import naga.toolkit.drawing.geometry.VPos;

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
