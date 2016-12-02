package naga.providers.toolkit.javafx.fx.view;

import naga.toolkit.fx.scene.shape.Circle;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.view.CircleView;

/**
 * @author Bruno Salmon
 */
public class FxCircleView extends FxShapeViewImpl<Circle, javafx.scene.shape.Circle> implements CircleView {

    @Override
    public void bind(Circle c, DrawingRequester drawingRequester) {
        super.bind(c, drawingRequester);
        fxNode.centerXProperty().bind(c.centerXProperty());
        fxNode.centerYProperty().bind(c.centerYProperty());
        fxNode.radiusProperty().bind(c.radiusProperty());
    }

    @Override
    javafx.scene.shape.Circle createFxNode(Circle node) {
        return new javafx.scene.shape.Circle();
    }
}
