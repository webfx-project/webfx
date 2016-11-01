package naga.providers.toolkit.javafx.drawing.view;

import naga.toolkit.drawing.shapes.Rectangle;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.view.RectangleView;

/**
 * @author Bruno Salmon
 */
public class FxRectangleView extends FxShapeViewImpl<Rectangle, javafx.scene.shape.Rectangle> implements RectangleView {

    @Override
    public void bind(Rectangle r, DrawingRequester drawingRequester) {
        setAndBindDrawableProperties(r, new javafx.scene.shape.Rectangle());
        fxDrawableNode.xProperty().bind(r.xProperty());
        fxDrawableNode.yProperty().bind(r.yProperty());
        fxDrawableNode.widthProperty().bind(r.widthProperty());
        fxDrawableNode.heightProperty().bind(r.heightProperty());
        fxDrawableNode.arcWidthProperty().bind(r.arcWidthProperty());
        fxDrawableNode.arcHeightProperty().bind(r.arcHeightProperty());
    }

}
