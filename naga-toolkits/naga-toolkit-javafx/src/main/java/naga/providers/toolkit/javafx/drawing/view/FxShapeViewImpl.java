package naga.providers.toolkit.javafx.drawing.view;

import naga.providers.toolkit.javafx.util.FxPaints;
import naga.providers.toolkit.javafx.util.FxStrokes;
import naga.toolkit.drawing.shapes.Shape;
import naga.toolkit.properties.conversion.ConvertedProperty;
import naga.toolkit.util.ObservableLists;

/**
 * @author Bruno Salmon
 */
abstract class FxShapeViewImpl<S extends Shape, FxS extends javafx.scene.shape.Shape> extends FxNodeViewImpl<S, FxS> implements FxShapeView<S, FxS> {

    void setAndBindNodeProperties(S node, FxS fxNode) {
        super.setAndBindNodeProperties(node, fxNode);
        fxNode.smoothProperty().bind(node.smoothProperty());
        fxNode.fillProperty().bind(new ConvertedProperty<>(node.fillProperty(), FxPaints::toFxPaint));
        fxNode.strokeProperty().bind(new ConvertedProperty<>(node.strokeProperty(), FxPaints::toFxPaint));
        fxNode.strokeWidthProperty().bind(node.strokeWidthProperty());
        fxNode.strokeLineCapProperty().bind(new ConvertedProperty<>(node.strokeLineCapProperty(), FxStrokes::toFxStrokeLineCap));
        fxNode.strokeLineJoinProperty().bind(new ConvertedProperty<>(node.strokeLineJoinProperty(), FxStrokes::toFxStrokeLineJoin));
        fxNode.strokeMiterLimitProperty().bind(node.strokeMiterLimitProperty());
        fxNode.strokeDashOffsetProperty().bind(node.strokeDashOffsetProperty());
        ObservableLists.bind(fxNode.getStrokeDashArray(), node.getStrokeDashArray());
    }

}
