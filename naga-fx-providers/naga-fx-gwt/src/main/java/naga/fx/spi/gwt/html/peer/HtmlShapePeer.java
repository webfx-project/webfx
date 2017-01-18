package naga.fx.spi.gwt.html.peer;

import elemental2.HTMLElement;
import naga.fx.spi.gwt.util.HtmlPaints;
import emul.javafx.scene.paint.Paint;
import emul.javafx.scene.shape.Shape;
import emul.javafx.scene.shape.StrokeLineCap;
import emul.javafx.scene.shape.StrokeLineJoin;
import emul.javafx.scene.shape.StrokeType;
import naga.fx.spi.peer.base.ShapePeerMixin;
import naga.fx.spi.peer.base.ShapePeerBase;

import java.util.List;

/**
 * @author Bruno Salmon
 */
abstract class HtmlShapePeer
        <N extends Shape, NB extends ShapePeerBase<N, NB, NM>, NM extends ShapePeerMixin<N, NB, NM>>

        extends HtmlNodePeer<N, NB, NM>
        implements ShapePeerMixin<N, NB, NM> {

    public HtmlShapePeer(NB base, HTMLElement element) {
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
