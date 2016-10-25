package naga.providers.toolkit.javafx.drawing.view;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import naga.providers.toolkit.javafx.util.FxPaints;
import naga.providers.toolkit.javafx.util.FxStrokes;
import naga.toolkit.drawing.shapes.Rectangle;
import naga.toolkit.drawing.spi.DrawingNotifier;
import naga.toolkit.drawing.spi.view.RectangleView;
import naga.toolkit.properties.conversion.ConvertedProperty;
import naga.toolkit.util.ObservableLists;

/**
 * @author Bruno Salmon
 */
public class FxRectangleView implements FxShapeView, RectangleView {

    private javafx.scene.shape.Rectangle fxRectangle;

    @Override
    public void bind(Rectangle rectangle, DrawingNotifier drawingNotifier) {
        fxRectangle = new javafx.scene.shape.Rectangle(0, 0, Color.RED);
        fxRectangle.xProperty().bind(rectangle.xProperty());
        fxRectangle.yProperty().bind(rectangle.yProperty());
        fxRectangle.widthProperty().bind(rectangle.widthProperty());
        fxRectangle.heightProperty().bind(rectangle.heightProperty());
        fxRectangle.arcWidthProperty().bind(rectangle.arcWidthProperty());
        fxRectangle.arcHeightProperty().bind(rectangle.arcHeightProperty());
        fxRectangle.fillProperty().bind(new ConvertedProperty<>(rectangle.fillProperty(), FxPaints::toFxPaint));
        fxRectangle.strokeProperty().bind(new ConvertedProperty<>(rectangle.strokeProperty(), FxPaints::toFxPaint));
        fxRectangle.strokeWidthProperty().bind(rectangle.strokeWidthProperty());
        fxRectangle.strokeLineCapProperty().bind(new ConvertedProperty<>(rectangle.strokeLineCapProperty(), FxStrokes::toFxStrokeLineCap));
        fxRectangle.strokeLineJoinProperty().bind(new ConvertedProperty<>(rectangle.strokeLineJoinProperty(), FxStrokes::toFxStrokeLineJoin));
        fxRectangle.strokeMiterLimitProperty().bind(rectangle.strokeMiterLimitProperty());
        fxRectangle.strokeDashOffsetProperty().bind(rectangle.strokeDashOffsetProperty());
        ObservableLists.bind(fxRectangle.getStrokeDashArray(), rectangle.getStrokeDashArray());
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
