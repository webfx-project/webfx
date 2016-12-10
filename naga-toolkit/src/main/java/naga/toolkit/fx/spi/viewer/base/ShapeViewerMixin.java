package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.paint.Paint;
import naga.toolkit.fx.scene.shape.Shape;
import naga.toolkit.fx.scene.shape.StrokeLineCap;
import naga.toolkit.fx.scene.shape.StrokeLineJoin;
import naga.toolkit.fx.spi.viewer.ShapeViewer;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public interface ShapeViewerMixin
        <N extends Shape, NV extends ShapeViewerBase<N, NV, NM>, NM extends ShapeViewerMixin<N, NV, NM>>

        extends ShapeViewer<N>,
        NodeViewerMixin<N, NV, NM> {

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
