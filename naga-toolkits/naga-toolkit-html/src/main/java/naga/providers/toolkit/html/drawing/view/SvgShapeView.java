package naga.providers.toolkit.html.drawing.view;

import elemental2.Element;
import naga.commons.util.collection.Collections;
import naga.providers.toolkit.html.util.SvgUtil;
import naga.toolkit.drawing.paint.Paint;
import naga.toolkit.drawing.shapes.Shape;
import naga.toolkit.drawing.shapes.StrokeLineCap;
import naga.toolkit.drawing.shapes.StrokeLineJoin;
import naga.toolkit.drawing.spi.view.base.ShapeViewBase;
import naga.toolkit.drawing.spi.view.base.ShapeViewMixin;

import java.util.List;

/**
 * @author Bruno Salmon
 */
abstract class SvgShapeView
        <D extends Shape, DV extends ShapeViewBase<D, DV, DM>, DM extends ShapeViewMixin<D, DV, DM>>
        extends SvgDrawableView<D, DV, DM>
        implements ShapeViewMixin<D, DV, DM> {

    public SvgShapeView(DV base, Element svgElement) {
        super(base, svgElement);
    }

    @Override
    public void updateFill(Paint fill) {
        setPaintAttribute("fill", fill);
    }

    @Override
    public void updateSmooth(Boolean smooth) {
        setSvgAttribute("shape-rendering", smooth ? "geometricPrecision" : "crispEdges");
    }

    @Override
    public void updateStroke(Paint stroke) {
        setPaintAttribute("stroke", stroke);
    }

    @Override
    public void updateStrokeWidth(Double strokeWidth) {
        setSvgAttribute("stroke-width", strokeWidth);
    }

    @Override
    public void updateStrokeLineCap(StrokeLineCap strokeLineCap) {
        setSvgAttribute("stroke-linecap", SvgUtil.toSvgStrokeLineCap(strokeLineCap));
    }

    @Override
    public void updateStrokeLineJoin(StrokeLineJoin strokeLineJoin) {
        setSvgAttribute("stroke-linejoin", SvgUtil.toSvgStrokeLineJoin(strokeLineJoin));
    }

    @Override
    public void updateStrokeMiterLimit(Double strokeMiterLimit) {
        setSvgAttribute("stroke-miterlimit", strokeMiterLimit);
    }

    @Override
    public void updateStrokeDashOffset(Double strokeDashOffset) {
        setSvgAttribute("stroke-dashoffset", strokeDashOffset);
    }

    @Override
    public void updateStrokeDashArray(List<Double> dashArray) {
        setSvgAttribute("stroke-dasharray", Collections.toStringWithNoBrackets(dashArray));
    }
}
