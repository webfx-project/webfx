package naga.providers.toolkit.javafx.fx.view;

import naga.providers.toolkit.javafx.util.FxFonts;
import naga.toolkit.fx.geometry.VPos;
import naga.toolkit.fx.scene.text.Font;
import naga.toolkit.fx.scene.text.Text;
import naga.toolkit.fx.scene.text.TextAlignment;
import naga.toolkit.fx.spi.view.base.TextViewBase;
import naga.toolkit.fx.spi.view.base.TextViewMixin;

/**
 * @author Bruno Salmon
 */
public class FxTextView
        extends FxShapeView<javafx.scene.text.Text, Text, TextViewBase, TextViewMixin>
        implements TextViewMixin {

    public FxTextView() {
        super(new TextViewBase());
    }

    @Override
    javafx.scene.text.Text createFxNode() {
        return new javafx.scene.text.Text();
    }

    @Override
    public void updateText(String text) {
        getFxNode().setText(text);
    }

    @Override
    public void updateFont(Font font) {
        getFxNode().setFont(FxFonts.toFxFont(font));
    }

    @Override
    public void updateTextOrigin(VPos textOrigin) {
        getFxNode().setTextOrigin(toFxVpos(textOrigin));
    }

    @Override
    public void updateX(Double x) {
        getFxNode().setX(x);
    }

    @Override
    public void updateY(Double y) {
        getFxNode().setY(y);
    }

    @Override
    public void updateWrappingWidth(Double wrappingWidth) {
        getFxNode().setWrappingWidth(wrappingWidth);
    }

    @Override
    public void updateTextAlignment(TextAlignment textAlignment) {
        getFxNode().setTextAlignment(toFxTextAlignment(textAlignment));
    }

    private static javafx.geometry.VPos toFxVpos(VPos vpos) {
        if (vpos != null)
            switch (vpos) {
                case TOP: return javafx.geometry.VPos.TOP;
                case CENTER: return javafx.geometry.VPos.CENTER;
                case BASELINE: return javafx.geometry.VPos.BASELINE;
                case BOTTOM: return javafx.geometry.VPos.BOTTOM;
            }
        return null;
    }

    private static javafx.scene.text.TextAlignment toFxTextAlignment(TextAlignment textAlignment) {
        if (textAlignment != null)
            switch (textAlignment) {
                case LEFT: return javafx.scene.text.TextAlignment.LEFT;
                case CENTER: return javafx.scene.text.TextAlignment.CENTER;
                case RIGHT: return javafx.scene.text.TextAlignment.RIGHT;
                case JUSTIFY: return javafx.scene.text.TextAlignment.JUSTIFY;
            }
        return null;
    }

}
