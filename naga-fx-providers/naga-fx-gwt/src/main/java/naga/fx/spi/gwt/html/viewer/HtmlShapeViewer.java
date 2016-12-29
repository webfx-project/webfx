package naga.fx.spi.gwt.html.viewer;

import elemental2.HTMLElement;
import naga.fx.spi.gwt.util.HtmlPaints;
import naga.fx.scene.paint.Paint;
import naga.fx.scene.shape.Shape;
import naga.fx.scene.shape.StrokeLineCap;
import naga.fx.scene.shape.StrokeLineJoin;
import naga.fx.scene.shape.StrokeType;
import naga.fx.spi.viewer.base.ShapeViewerBase;
import naga.fx.spi.viewer.base.ShapeViewerMixin;

import java.util.List;

/**
 * @author Bruno Salmon
 */
abstract class HtmlShapeViewer
        <N extends Shape, NB extends ShapeViewerBase<N, NB, NM>, NM extends ShapeViewerMixin<N, NB, NM>>

        extends HtmlNodeViewer<N, NB, NM>
        implements ShapeViewerMixin<N, NB, NM> {

    public HtmlShapeViewer(NB base, HTMLElement element) {
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

    @Override
    public void updateStrokeType(StrokeType strokeType) {
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
