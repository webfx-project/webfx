package naga.toolkit.drawing.spi.view.base;

import naga.toolkit.drawing.paint.Paint;
import naga.toolkit.drawing.shapes.Shape;
import naga.toolkit.drawing.shapes.StrokeLineCap;
import naga.toolkit.drawing.shapes.StrokeLineJoin;
import naga.toolkit.drawing.spi.view.ShapeView;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public interface ShapeViewMixin
        <D extends Shape, DV extends ShapeViewBase<D, DV, DM>, DM extends ShapeViewMixin<D, DV, DM>>

        extends ShapeView<D>,
                DrawableViewMixin<D, DV, DM> {

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
