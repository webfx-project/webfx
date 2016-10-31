package naga.providers.toolkit.javafx.drawing.view;

import javafx.scene.text.Text;
import naga.providers.toolkit.javafx.util.FxFonts;
import naga.toolkit.drawing.shapes.TextAlignment;
import naga.toolkit.drawing.shapes.TextShape;
import naga.toolkit.drawing.shapes.VPos;
import naga.toolkit.drawing.spi.DrawingNotifier;
import naga.toolkit.drawing.spi.view.TextShapeView;
import naga.toolkit.properties.conversion.ConvertedProperty;

/**
 * @author Bruno Salmon
 */
public class FxTextShapeView extends FxShapeViewImpl<TextShape, Text> implements TextShapeView {

    @Override
    public void bind(TextShape shape, DrawingNotifier drawingNotifier) {
        setAndBindCommonShapeProperties(shape, new Text());
        fxShape.xProperty().bind(shape.xProperty());
        fxShape.yProperty().bind(shape.yProperty());
        fxShape.textProperty().bind(shape.textProperty());
        fxShape.textOriginProperty().bind(new ConvertedProperty<>(shape.textOriginProperty(), FxTextShapeView::toFxVpos));
        fxShape.textAlignmentProperty().bind(new ConvertedProperty<>(shape.textAlignmentProperty(), FxTextShapeView::toFxTextAlignment));
        fxShape.wrappingWidthProperty().bind(shape.wrappingWidthProperty());
        fxShape.fontProperty().bind(new ConvertedProperty<>(shape.fontProperty(), FxFonts::toFxFont));
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
