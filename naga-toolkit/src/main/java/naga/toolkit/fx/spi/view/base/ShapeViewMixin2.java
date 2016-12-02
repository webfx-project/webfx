package naga.toolkit.fx.spi.view.base;

import naga.toolkit.fx.scene.paint.Paint;
import naga.toolkit.fx.scene.shape.Shape;
import naga.toolkit.fx.scene.shape.StrokeLineCap;
import naga.toolkit.fx.scene.shape.StrokeLineJoin;

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
