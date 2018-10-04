package webfx.fxkit.mapper.spi.impl.peer;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public interface ShapePeerMixin
        <N extends Shape, NB extends ShapePeerBase<N, NB, NM>, NM extends ShapePeerMixin<N, NB, NM>>

        extends NodePeerMixin<N, NB, NM> {

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
