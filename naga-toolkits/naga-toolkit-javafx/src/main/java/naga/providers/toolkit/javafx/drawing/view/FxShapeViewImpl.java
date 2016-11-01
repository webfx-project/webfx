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

    void setAndBindDrawableProperties(S shape, N fxDrawableNode) {
        super.setAndBindDrawableProperties(shape, fxDrawableNode);
        fxDrawableNode.smoothProperty().bind(shape.smoothProperty());
        fxDrawableNode.fillProperty().bind(new ConvertedProperty<>(shape.fillProperty(), FxPaints::toFxPaint));
        fxDrawableNode.strokeProperty().bind(new ConvertedProperty<>(shape.strokeProperty(), FxPaints::toFxPaint));
        fxDrawableNode.strokeWidthProperty().bind(shape.strokeWidthProperty());
        fxDrawableNode.strokeLineCapProperty().bind(new ConvertedProperty<>(shape.strokeLineCapProperty(), FxStrokes::toFxStrokeLineCap));
        fxDrawableNode.strokeLineJoinProperty().bind(new ConvertedProperty<>(shape.strokeLineJoinProperty(), FxStrokes::toFxStrokeLineJoin));
        fxDrawableNode.strokeMiterLimitProperty().bind(shape.strokeMiterLimitProperty());
        fxDrawableNode.strokeDashOffsetProperty().bind(shape.strokeDashOffsetProperty());
        ObservableLists.bind(fxDrawableNode.getStrokeDashArray(), shape.getStrokeDashArray());
    }

}
