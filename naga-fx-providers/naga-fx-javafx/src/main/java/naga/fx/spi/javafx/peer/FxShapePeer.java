package naga.fx.spi.javafx.peer;

import naga.fx.spi.javafx.util.FxPaints;
import naga.fx.spi.javafx.util.FxStrokes;
import naga.fx.scene.paint.Paint;
import naga.fx.scene.shape.Shape;
import naga.fx.scene.shape.StrokeLineCap;
import naga.fx.scene.shape.StrokeLineJoin;
import naga.fx.scene.shape.StrokeType;
import naga.fx.spi.peer.base.ShapePeerMixin;
import naga.fx.spi.peer.base.ShapePeerBase;

import java.util.List;

/**
 * @author Bruno Salmon
 */
abstract class FxShapePeer
        <FxN extends javafx.scene.shape.Shape, N extends Shape, NB extends ShapePeerBase<N, NB, NM>, NM extends ShapePeerMixin<N, NB, NM>>

        extends FxNodePeer<FxN, N, NB, NM>
        implements ShapePeerMixin<N, NB, NM>, FxLayoutMeasurable {

    FxShapePeer(NB base) {
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
    public void updateStrokeType(StrokeType strokeType) {
        getFxNode().setStrokeType(FxStrokes.toFxStrokeType(strokeType));
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
