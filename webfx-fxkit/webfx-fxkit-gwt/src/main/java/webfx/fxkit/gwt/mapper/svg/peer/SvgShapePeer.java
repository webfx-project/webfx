package webfx.fxkit.gwt.mapper.svg.peer;

import elemental2.dom.Element;
import webfx.fxkits.core.mapper.spi.impl.peer.ShapePeerBase;
import webfx.fxkits.core.mapper.spi.impl.peer.ShapePeerMixin;
import webfx.platform.shared.util.collection.Collections;
import webfx.fxkit.gwt.mapper.util.SvgUtil;
import emul.javafx.scene.paint.Paint;
import emul.javafx.scene.shape.Shape;
import emul.javafx.scene.shape.StrokeLineCap;
import emul.javafx.scene.shape.StrokeLineJoin;
import emul.javafx.scene.shape.StrokeType;

import java.util.List;

/**
 * @author Bruno Salmon
 */
abstract class SvgShapePeer
        <N extends Shape, NB extends ShapePeerBase<N, NB, NM>, NM extends ShapePeerMixin<N, NB, NM>>

        extends SvgNodePeer<N, NB, NM>
        implements ShapePeerMixin<N, NB, NM> {

    public SvgShapePeer(NB base, Element element) {
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
    public void updateStrokeType(StrokeType strokeType) {
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
        setElementAttribute("stroke-alignment", hasStroke ? SvgUtil.toSvgStrokeAlignment(n.getStrokeType()) : null);
    }
}
