package naga.providers.toolkit.javafx.drawing.view;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import naga.toolkit.drawing.shapes.Rectangle;
import naga.toolkit.drawing.spi.DrawingNotifier;
import naga.toolkit.drawing.spi.view.RectangleView;

/**
 * @author Bruno Salmon
 */
public class FxRectangleView implements FxShapeView, RectangleView {

    javafx.scene.shape.Rectangle fxRectangle;

    @Override
    public void bind(Rectangle rectangle, DrawingNotifier drawingNotifier) {
        fxRectangle = new javafx.scene.shape.Rectangle(0, 0, Color.RED);
        fxRectangle.xProperty().bind(rectangle.xProperty());
        fxRectangle.yProperty().bind(rectangle.yProperty());
        fxRectangle.widthProperty().bind(rectangle.widthProperty());
        fxRectangle.heightProperty().bind(rectangle.heightProperty());
    }

    @Override
    public void unbind() {
        fxRectangle = null;
    }

    @Override
    public Node getFxShapeNode() {
        return fxRectangle;
    }

}
