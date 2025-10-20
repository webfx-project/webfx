package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.svg;

import dev.webfx.kit.mapper.peers.javafxgraphics.base.ShapePeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.ShapePeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.SvgUtil;
import dev.webfx.platform.util.collection.Collections;
import elemental2.dom.Element;
import elemental2.svg.SVGLocatable;
import elemental2.svg.SVGRect;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public abstract class SvgShapePeer
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
        if (n != null) {
            Paint stroke = n.getStroke();
            boolean hasStroke = stroke != null && getNode().getStrokeWidth() > 0;
            setPaintAttribute("stroke", stroke);
            setElementAttribute("stroke-width", hasStroke ? n.getStrokeWidth() : null);
            setElementAttribute("stroke-linecap", hasStroke ? SvgUtil.toSvgStrokeLineCap(n.getStrokeLineCap()) : null);
            setElementAttribute("stroke-linejoin", hasStroke ? SvgUtil.toSvgStrokeLineJoin(n.getStrokeLineJoin()) : null);
            setElementAttribute("stroke-miterlimit", hasStroke ? n.getStrokeMiterLimit() : null);
            setElementAttribute("stroke-dashoffset", hasStroke ? n.getStrokeDashOffset() : null);
            setElementAttribute("stroke-dasharray", hasStroke ? Collections.toStringCommaSeparated(n.getStrokeDashArray()) : null);
            setElementAttribute("stroke-alignment", hasStroke ? SvgUtil.toSvgStrokeAlignment(n.getStrokeType()) : null);
        }
    }

    @Override
    public void updateAllNodeTransforms(List<Transform> allNodeTransforms) {
        // We also need to consider here a difference between standard SVG and JavaFX regarding the coordinate system:
        // - In standard SVG, the top left corner of the viewport always refers to (x=0,y=0) even if part of the shape
        //   is negative (so this negative part actually won't show on the viewport)
        // - In JavaFX, the top left corner of the viewport also refers to (x=0,y=0) in general, except a part of the
        //   shape is negative. In that case, the coordinate system is shifted so that the negative part of the shape
        //   automatically appears on the viewport
        // We need here to emulate the same behavior as JavaFX by translating the shape when part of it is negative.
        SVGRect bBox = getBBox();
        if (bBox != null && (bBox.x < 0 || bBox.y < 0)) {
            List<Transform> forSvgTransforms = new ArrayList<>(allNodeTransforms.size() + 1);
            forSvgTransforms.add(new Translate(Math.max(-bBox.x, 0), Math.max(-bBox.y, 0)));
            forSvgTransforms.addAll(allNodeTransforms);
            allNodeTransforms = forSvgTransforms;
        }
        super.updateAllNodeTransforms(allNodeTransforms);
    }

    public SVGRect getBBox() {
        return ((SVGLocatable) getElement()).getBBox();
    }

}
