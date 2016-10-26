package naga.providers.toolkit.javafx.drawing.view;

import naga.toolkit.drawing.shapes.Rectangle;
import naga.toolkit.drawing.spi.DrawingNotifier;
import naga.toolkit.drawing.spi.view.RectangleView;

/**
 * @author Bruno Salmon
 */
public class FxRectangleView extends FxShapeViewImpl<Rectangle, javafx.scene.shape.Rectangle> implements RectangleView {

    @Override
    public void bind(Rectangle shape, DrawingNotifier drawingNotifier) {
        setAndBindCommonShapeProperties(shape, new javafx.scene.shape.Rectangle());
        fxShape.xProperty().bind(shape.xProperty());
        fxShape.yProperty().bind(shape.yProperty());
        fxShape.widthProperty().bind(shape.widthProperty());
        fxShape.heightProperty().bind(shape.heightProperty());
        fxShape.arcWidthProperty().bind(shape.arcWidthProperty());
        fxShape.arcHeightProperty().bind(shape.arcHeightProperty());
    }

}
