package naga.providers.toolkit.javafx.drawing.view;

import naga.toolkit.drawing.shapes.Circle;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.view.CircleView;

/**
 * @author Bruno Salmon
 */
public class FxCircleView extends FxShapeViewImpl<Circle, javafx.scene.shape.Circle> implements CircleView {

    @Override
    public void bind(Circle c, DrawingRequester drawingRequester) {
        setAndBindDrawableProperties(c, new javafx.scene.shape.Circle());
        fxDrawableNode.centerXProperty().bind(c.centerXProperty());
        fxDrawableNode.centerYProperty().bind(c.centerYProperty());
        fxDrawableNode.radiusProperty().bind(c.radiusProperty());
    }
}
