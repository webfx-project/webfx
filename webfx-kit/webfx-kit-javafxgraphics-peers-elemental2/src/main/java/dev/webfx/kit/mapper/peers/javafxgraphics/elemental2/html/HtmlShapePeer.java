package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html;

import elemental2.dom.HTMLElement;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlPaints;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.ShapePeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.ShapePeerBase;

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

    protected void updateStroke() {
        N shape = getNode();
        String color = HtmlPaints.toHtmlCssPaint(shape.getStroke());
        double strokeWidth = shape.getStrokeWidth();
        boolean hasStroke = color != null && strokeWidth > 0;
        setElementStyleAttribute("border-color", hasStroke ? color : null);
        setElementStyleAttribute("border-style", hasStroke ? "solid" : null);
        setElementStyleAttribute("border-width", hasStroke ? toPx(strokeWidth) : null);
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
