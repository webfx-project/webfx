package naga.fx.spi.viewer.base;

import naga.fx.scene.paint.Paint;
import naga.fx.scene.shape.Shape;
import naga.fx.scene.shape.StrokeLineCap;
import naga.fx.scene.shape.StrokeLineJoin;
import naga.fx.scene.shape.StrokeType;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public interface ShapeViewerMixin
        <N extends Shape, NB extends ShapeViewerBase<N, NB, NM>, NM extends ShapeViewerMixin<N, NB, NM>>

        extends NodeViewerMixin<N, NB, NM> {

    void updateFill(Paint fill);

    void updateSmooth(Boolean smooth);

    void updateStroke(Paint stroke);

    void updateStrokeWidth(Double strokeWidth);

    void updateStrokeLineCap(StrokeLineCap strokeLineCap);

    void updateStrokeLineJoin(StrokeLineJoin strokeLineJoin);

    void updateStrokeMiterLimit(Double strokeMiterLimit);

    void updateStrokeDashOffset(Double strokeDashOffset);

    void updateStrokeDashArray(List<Double> dashArray);

    void updateStrokeType(StrokeType strokeType);
}
