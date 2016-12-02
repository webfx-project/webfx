package naga.providers.toolkit.javafx.fx.view;

import naga.providers.toolkit.javafx.util.FxFonts;
import naga.toolkit.fx.scene.text.Text;
import naga.toolkit.fx.scene.text.TextAlignment;
import naga.toolkit.fx.geometry.VPos;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.view.TextView;
import naga.toolkit.properties.conversion.ConvertedProperty;

/**
 * @author Bruno Salmon
 */
public class FxTextView extends FxShapeViewImpl<Text, javafx.scene.text.Text> implements TextView {

    @Override
    public void bind(Text t, DrawingRequester drawingRequester) {
        super.bind(t, drawingRequester);
        fxNode.xProperty().bind(t.xProperty());
        fxNode.yProperty().bind(t.yProperty());
        fxNode.textProperty().bind(t.textProperty());
        fxNode.textOriginProperty().bind(new ConvertedProperty<>(t.textOriginProperty(), FxTextView::toFxVpos));
        fxNode.textAlignmentProperty().bind(new ConvertedProperty<>(t.textAlignmentProperty(), FxTextView::toFxTextAlignment));
        fxNode.wrappingWidthProperty().bind(t.wrappingWidthProperty());
        fxNode.fontProperty().bind(new ConvertedProperty<>(t.fontProperty(), FxFonts::toFxFont));
    }

    @Override
    javafx.scene.text.Text createFxNode(Text node) {
        return new javafx.scene.text.Text();
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
