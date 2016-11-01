package naga.providers.toolkit.javafx.drawing.view;

import naga.providers.toolkit.javafx.util.FxPaints;
import naga.providers.toolkit.javafx.util.FxStrokes;
import naga.toolkit.drawing.shapes.Shape;
import naga.toolkit.properties.conversion.ConvertedProperty;
import naga.toolkit.util.ObservableLists;

/**
 * @author Bruno Salmon
 */
abstract class FxShapeViewImpl<S extends Shape, N extends javafx.scene.shape.Shape> extends FxDrawableViewImpl<S, N> implements FxShapeView<S, N> {

    void setAndBindDrawableProperties(S shape, N fxShape) {
        super.setAndBindDrawableProperties(shape, fxShape);
        fxShape.smoothProperty().bind(shape.smoothProperty());
        fxShape.fillProperty().bind(new ConvertedProperty<>(shape.fillProperty(), FxPaints::toFxPaint));
        fxShape.strokeProperty().bind(new ConvertedProperty<>(shape.strokeProperty(), FxPaints::toFxPaint));
        fxShape.strokeWidthProperty().bind(shape.strokeWidthProperty());
        fxShape.strokeLineCapProperty().bind(new ConvertedProperty<>(shape.strokeLineCapProperty(), FxStrokes::toFxStrokeLineCap));
        fxShape.strokeLineJoinProperty().bind(new ConvertedProperty<>(shape.strokeLineJoinProperty(), FxStrokes::toFxStrokeLineJoin));
        fxShape.strokeMiterLimitProperty().bind(shape.strokeMiterLimitProperty());
        fxShape.strokeDashOffsetProperty().bind(shape.strokeDashOffsetProperty());
        ObservableLists.bind(fxShape.getStrokeDashArray(), shape.getStrokeDashArray());
    }

}
