package naga.providers.toolkit.javafx.drawing.view;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import naga.toolkit.drawing.shapes.TextShape;
import naga.toolkit.drawing.spi.DrawingNotifier;
import naga.toolkit.drawing.spi.view.TextShapeView;

/**
 * @author Bruno Salmon
 */
public class FxTextShapeView extends FxShapeViewImpl<TextShape, Text> implements TextShapeView {

    private final Font font = Font.font("Serif", 100);
    @Override
    public void bind(TextShape shape, DrawingNotifier drawingNotifier) {
        setAndBindCommonShapeProperties(shape, new Text());
        fxShape.xProperty().bind(shape.xProperty());
        fxShape.yProperty().bind(shape.yProperty());
        fxShape.textProperty().bind(shape.textProperty());
        fxShape.setFont(font);
    }

}
