package naga.toolkit.drawing.spi.view.base;

import naga.toolkit.drawing.paint.Paint;
import naga.toolkit.drawing.shape.Shape;
import naga.toolkit.drawing.shape.StrokeLineCap;
import naga.toolkit.drawing.shape.StrokeLineJoin;
import naga.toolkit.drawing.spi.view.ShapeView;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public interface ShapeViewMixin
        <N extends Shape, NV extends ShapeViewBase<N, NV, NM>, NM extends ShapeViewMixin<N, NV, NM>>

        extends ShapeView<N>,
        NodeViewMixin<N, NV, NM> {

    void updateFill(Paint fill);

    void updateSmooth(Boolean smooth);

    void updateStroke(Paint stroke);

    void updateStrokeWidth(Double strokeWidth);

    void updateStrokeLineCap(StrokeLineCap strokeLineCap);

    void updateStrokeLineJoin(StrokeLineJoin strokeLineJoin);

    void updateStrokeMiterLimit(Double strokeMiterLimit);

    void updateStrokeDashOffset(Double strokeDashOffset);

    void updateStrokeDashArray(List<Double> dashArray);

}
