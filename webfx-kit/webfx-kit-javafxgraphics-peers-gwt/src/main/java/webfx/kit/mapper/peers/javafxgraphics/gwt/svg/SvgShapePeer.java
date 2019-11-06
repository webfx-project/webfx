package webfx.kit.mapper.peers.javafxgraphics.gwt.svg;

import elemental2.dom.Element;
import elemental2.svg.SVGRect;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import webfx.kit.mapper.peers.javafxgraphics.gwt.util.SvgUtil;
import webfx.kit.mapper.peers.javafxgraphics.base.ShapePeerBase;
import webfx.kit.mapper.peers.javafxgraphics.base.ShapePeerMixin;
import webfx.platform.shared.util.collection.Collections;

import java.util.ArrayList;
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

    @Override
    public void updateLocalToParentTransforms(List<Transform> localToParentTransforms) {
        // Before transformation, the top left corner of the container refers to the axis origin (0,0) in SVG whereas it
        // refers to the top left corner of the path in JavaFx. So to emulate the same behavior as JavaFx, we need to
        // add a translation of the shape so it appears on the top left corner.
        SVGRect bBox = getBBox();
        if (bBox != null) {
            List<Transform> forSvgTransforms = new ArrayList<>(localToParentTransforms.size() + 1);
            forSvgTransforms.add(new Translate(-bBox.x, -bBox.y));
            forSvgTransforms.addAll(localToParentTransforms);
            localToParentTransforms = forSvgTransforms;
        }
        super.updateLocalToParentTransforms(localToParentTransforms);
    }

    protected SVGRect getBBox() {
        return null;
    }

}
