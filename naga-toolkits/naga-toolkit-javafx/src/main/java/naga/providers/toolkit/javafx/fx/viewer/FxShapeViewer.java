package naga.providers.toolkit.javafx.fx.viewer;

import naga.providers.toolkit.javafx.util.FxPaints;
import naga.providers.toolkit.javafx.util.FxStrokes;
import naga.toolkit.fx.scene.paint.Paint;
import naga.toolkit.fx.scene.shape.Shape;
import naga.toolkit.fx.scene.shape.StrokeLineCap;
import naga.toolkit.fx.scene.shape.StrokeLineJoin;
import naga.toolkit.fx.spi.viewer.base.ShapeViewerBase;
import naga.toolkit.fx.spi.viewer.base.ShapeViewerMixin;

import java.util.List;

/**
 * @author Bruno Salmon
 */
abstract class FxShapeViewer
        <FxN extends javafx.scene.shape.Shape, N extends Shape, NV extends ShapeViewerBase<N, NV, NM>, NM extends ShapeViewerMixin<N, NV, NM>>

        extends FxNodeViewer<FxN, N, NV, NM>
        implements ShapeViewerMixin<N, NV, NM>, FxLayoutMeasurable {

    FxShapeViewer(NV base) {
        super(base);
    }

    @Override
    public void updateSmooth(Boolean smooth) {
        getFxNode().setSmooth(smooth);
    }

    @Override
    public void updateFill(Paint fill) {
        getFxNode().setFill(FxPaints.toFxPaint(fill));
    }

    @Override
    public void updateStroke(Paint stroke) {
        getFxNode().setStroke(FxPaints.toFxPaint(stroke));
    }

    @Override
    public void updateStrokeWidth(Double strokeWidth) {
        getFxNode().setStrokeWidth(strokeWidth);
    }

    @Override
    public void updateStrokeLineCap(StrokeLineCap strokeLineCap) {
        getFxNode().setStrokeLineCap(FxStrokes.toFxStrokeLineCap(strokeLineCap));
    }

    @Override
    public void updateStrokeLineJoin(StrokeLineJoin strokeLineJoin) {
        getFxNode().setStrokeLineJoin(FxStrokes.toFxStrokeLineJoin(strokeLineJoin));
    }

    @Override
    public void updateStrokeMiterLimit(Double strokeMiterLimit) {
        getFxNode().setStrokeMiterLimit(strokeMiterLimit);
    }

    @Override
    public void updateStrokeDashOffset(Double strokeDashOffset) {
        getFxNode().setStrokeDashOffset(strokeDashOffset);
    }

    @Override
    public void updateStrokeDashArray(List<Double> dashArray) {
        getFxNode().getStrokeDashArray().setAll(dashArray);
    }

}
