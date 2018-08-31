package webfx.fx.spi.javafx.peer;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import webfx.fx.spi.peer.base.ShapePeerMixin;
import webfx.fx.spi.peer.base.ShapePeerBase;

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
        getFxNode().setFill(fill);
    }

    @Override
    public void updateStroke(Paint stroke) {
        getFxNode().setStroke(stroke);
    }

    @Override
    public void updateStrokeType(StrokeType strokeType) {
        getFxNode().setStrokeType(strokeType);
    }

    @Override
    public void updateStrokeWidth(Double strokeWidth) {
        getFxNode().setStrokeWidth(strokeWidth);
    }

    @Override
    public void updateStrokeLineCap(StrokeLineCap strokeLineCap) {
        getFxNode().setStrokeLineCap(strokeLineCap);
    }

    @Override
    public void updateStrokeLineJoin(StrokeLineJoin strokeLineJoin) {
        getFxNode().setStrokeLineJoin(strokeLineJoin);
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
