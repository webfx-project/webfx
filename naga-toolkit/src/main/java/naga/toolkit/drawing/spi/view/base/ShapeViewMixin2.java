package naga.toolkit.drawing.spi.view.base;

import naga.toolkit.drawing.paint.Paint;
import naga.toolkit.drawing.shapes.Shape;
import naga.toolkit.drawing.shapes.StrokeLineCap;
import naga.toolkit.drawing.shapes.StrokeLineJoin;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public interface ShapeViewMixin2
        <D extends Shape, DV extends ShapeViewBase<D, DV, DM>, DM extends ShapeViewMixin2<D, DV, DM>>

        extends ShapeViewMixin<D, DV, DM> {

    default void updateFill(Paint fill) {}

    default void updateSmooth(Boolean smooth) {}

    default void updateStroke(Paint stroke) {}

    default void updateStrokeWidth(Double strokeWidth) {}

    default void updateStrokeLineCap(StrokeLineCap strokeLineCap) {}

    default void updateStrokeLineJoin(StrokeLineJoin strokeLineJoin) {}

    default void updateStrokeMiterLimit(Double strokeMiterLimit) {}

    default void updateStrokeDashOffset(Double strokeDashOffset) {}

    default void updateStrokeDashArray(List<Double> dashArray) {};

}
