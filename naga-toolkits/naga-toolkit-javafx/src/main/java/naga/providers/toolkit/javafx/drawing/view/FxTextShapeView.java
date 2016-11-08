package naga.providers.toolkit.javafx.drawing.view;

import javafx.scene.text.Text;
import naga.providers.toolkit.javafx.util.FxFonts;
import naga.toolkit.drawing.shapes.TextAlignment;
import naga.toolkit.drawing.shapes.TextShape;
import naga.toolkit.drawing.shapes.VPos;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.view.TextShapeView;
import naga.toolkit.properties.conversion.ConvertedProperty;

/**
 * @author Bruno Salmon
 */
public class FxTextShapeView extends FxShapeViewImpl<TextShape, Text> implements TextShapeView {

    @Override
    public void bind(TextShape ts, DrawingRequester drawingRequester) {
        setAndBindDrawableProperties(ts, new Text());
        fxDrawableNode.xProperty().bind(ts.xProperty());
        fxDrawableNode.yProperty().bind(ts.yProperty());
        fxDrawableNode.textProperty().bind(ts.textProperty());
        fxDrawableNode.textOriginProperty().bind(new ConvertedProperty<>(ts.textOriginProperty(), FxTextShapeView::toFxVpos));
        fxDrawableNode.textAlignmentProperty().bind(new ConvertedProperty<>(ts.textAlignmentProperty(), FxTextShapeView::toFxTextAlignment));
        fxDrawableNode.wrappingWidthProperty().bind(ts.wrappingWidthProperty());
        fxDrawableNode.fontProperty().bind(new ConvertedProperty<>(ts.fontProperty(), FxFonts::toFxFont));
        fxDrawableNode.setMouseTransparent(true); // temporary as text shapes are usually not clickale in Mongoose
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
