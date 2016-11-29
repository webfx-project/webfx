package naga.providers.toolkit.html.drawing.svg.view;

import elemental2.Element;
import naga.commons.util.collection.Collections;
import naga.providers.toolkit.html.util.SvgUtil;
import naga.toolkit.drawing.paint.Paint;
import naga.toolkit.drawing.shape.Shape;
import naga.toolkit.drawing.shape.StrokeLineCap;
import naga.toolkit.drawing.shape.StrokeLineJoin;
import naga.toolkit.drawing.spi.view.base.ShapeViewBase;
import naga.toolkit.drawing.spi.view.base.ShapeViewMixin;

import java.util.List;

/**
 * @author Bruno Salmon
 */
abstract class SvgShapeView
        <N extends Shape, NV extends ShapeViewBase<N, NV, NM>, NM extends ShapeViewMixin<N, NV, NM>>
        extends SvgNodeView<N, NV, NM>
        implements ShapeViewMixin<N, NV, NM> {

    public SvgShapeView(NV base, Element element) {
        super(base, element);
    }

    @Override
    public void updateFill(Paint fill) {
        setPaintAttribute("fill", fill);
    }

    @Override
    public void updateSmooth(Boolean smooth) {
        setElementAttribute("shape-rendering", smooth ? "geometricPrecision" : "crispEdges");
    }

    @Override
    public void updateStroke(Paint stroke) {
        updateSvgStroke();
    }

    @Override
    public void updateStrokeWidth(Double strokeWidth) {
        updateSvgStroke();
    }

    @Override
    public void updateStrokeLineCap(StrokeLineCap strokeLineCap) {
        updateSvgStroke();
    }

    @Override
    public void updateStrokeLineJoin(StrokeLineJoin strokeLineJoin) {
        updateSvgStroke();
    }

    @Override
    public void updateStrokeMiterLimit(Double strokeMiterLimit) {
        updateSvgStroke();
    }

    @Override
    public void updateStrokeDashOffset(Double strokeDashOffset) {
        updateSvgStroke();
    }

    @Override
    public void updateStrokeDashArray(List<Double> dashArray) {
        updateSvgStroke();
    }

    private void updateSvgStroke() {
        N n = getNode();
        Paint stroke = n.getStroke();
        boolean hasStroke = stroke != null && getNode().getStrokeWidth() > 0;
        setPaintAttribute("stroke", stroke);
        setElementAttribute("stroke-width", hasStroke ? n.getStrokeWidth() : null);
        setElementAttribute("stroke-linecap", hasStroke ? SvgUtil.toSvgStrokeLineCap(n.getStrokeLineCap()) : null);
        setElementAttribute("stroke-linejoin", hasStroke ? SvgUtil.toSvgStrokeLineJoin(n.getStrokeLineJoin()) : null);
        setElementAttribute("stroke-miterlimit", hasStroke ? n.getStrokeMiterLimit() : null);
        setElementAttribute("stroke-dashoffset", hasStroke ? n.getStrokeDashOffset() : null);
        setElementAttribute("stroke-dasharray", hasStroke ? Collections.toStringWithNoBrackets(n.getStrokeDashArray()) : null);
    }
}
