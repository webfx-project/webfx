package naga.providers.toolkit.javafx.fx.viewer;

import naga.providers.toolkit.javafx.util.FxFonts;
import naga.toolkit.fx.geometry.VPos;
import naga.toolkit.fx.scene.text.Font;
import naga.toolkit.fx.scene.text.Text;
import naga.toolkit.fx.scene.text.TextAlignment;
import naga.toolkit.fx.spi.viewer.base.TextViewerBase;
import naga.toolkit.fx.spi.viewer.base.TextViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxTextViewer
        <FxN extends javafx.scene.text.Text, N extends Text, NB extends TextViewerBase<N, NB, NM>, NM extends TextViewerMixin<N, NB, NM>>
        extends FxShapeViewer<FxN, N, NB, NM>
        implements TextViewerMixin<N, NB, NM>, FxLayoutMeasurable {

    public FxTextViewer() {
        this((NB) new TextViewerBase());
    }

    FxTextViewer(NB base) {
        super(base);
    }

    @Override
    protected FxN createFxNode() {
        return (FxN) new javafx.scene.text.Text();
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
