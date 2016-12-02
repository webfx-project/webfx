package naga.providers.toolkit.html.fx.html.view;

import elemental2.HTMLElement;
import naga.providers.toolkit.html.util.HtmlPaints;
import naga.toolkit.fx.scene.paint.Paint;
import naga.toolkit.fx.scene.shape.Shape;
import naga.toolkit.fx.scene.shape.StrokeLineCap;
import naga.toolkit.fx.scene.shape.StrokeLineJoin;
import naga.toolkit.fx.spi.view.base.ShapeViewBase;
import naga.toolkit.fx.spi.view.base.ShapeViewMixin;

import java.util.List;

/**
 * @author Bruno Salmon
 */
abstract class HtmlShapeView
        <N extends Shape, NV extends ShapeViewBase<N, NV, NM>, NM extends ShapeViewMixin<N, NV, NM>>
        extends HtmlNodeView<N, NV, NM>
        implements ShapeViewMixin<N, NV, NM> {

    public HtmlShapeView(NV base, HTMLElement element) {
        super(base, element);
    }

    @Override
    public void updateFill(Paint fill) {
        getElement().style.background = HtmlPaints.toHtmlCssPaint(fill);
    }

    @Override
    public void updateSmooth(Boolean smooth) {
        //setElementAttribute("shape-rendering", smooth ? "geometricPrecision" : "crispEdges");
    }

    @Override
    public void updateStroke(Paint stroke) {
        updateStroke();
    }

    private void updateStroke() {
        N shape = getNode();
        String color = HtmlPaints.toHtmlCssPaint(shape.getStroke());
        Double strokeWidth = shape.getStrokeWidth();
        boolean hasStroke = color != null && strokeWidth > 0;
        setElementStyleAttribute("border-color", hasStroke ? color : null);
        setElementStyleAttribute("border-style", hasStroke ? "solid" : null);
        setElementStyleAttribute("border-width", hasStroke ? strokeWidth + "px" : null);
    }

    @Override
    public void updateStrokeWidth(Double strokeWidth) {
        updateStroke();
    }

    @Override
    public void updateStrokeLineCap(StrokeLineCap strokeLineCap) {
        updateStroke();
    }

    @Override
    public void updateStrokeLineJoin(StrokeLineJoin strokeLineJoin) {
        updateStroke();
    }

    @Override
    public void updateStrokeMiterLimit(Double strokeMiterLimit) {
        updateStroke();
    }

    @Override
    public void updateStrokeDashOffset(Double strokeDashOffset) {
        updateStroke();
    }

    @Override
    public void updateStrokeDashArray(List<Double> dashArray) {
        updateStroke();
    }
}
