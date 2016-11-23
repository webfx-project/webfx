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
        super.bind(ts, drawingRequester);
        fxNode.xProperty().bind(ts.xProperty());
        fxNode.yProperty().bind(ts.yProperty());
        fxNode.textProperty().bind(ts.textProperty());
        fxNode.textOriginProperty().bind(new ConvertedProperty<>(ts.textOriginProperty(), FxTextShapeView::toFxVpos));
        fxNode.textAlignmentProperty().bind(new ConvertedProperty<>(ts.textAlignmentProperty(), FxTextShapeView::toFxTextAlignment));
        fxNode.wrappingWidthProperty().bind(ts.wrappingWidthProperty());
        fxNode.fontProperty().bind(new ConvertedProperty<>(ts.fontProperty(), FxFonts::toFxFont));
        fxNode.setMouseTransparent(true); // temporary as text shapes are usually not clickale in Mongoose
    }

    @Override
    Text createFxNode(TextShape node) {
        return new Text();
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
