package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import elemental2.dom.*;
import elemental2.svg.SVGRect;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.SVGPathPeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.SVGPathPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.layoutmeasurable.HtmlLayoutCache;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.layoutmeasurable.HtmlLayoutMeasurableNoGrow;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.shared.SvgRoot;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.shared.SvgRootBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.svg.SvgPathPeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.SvgUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class HtmlSVGPathPeer
        <N extends SVGPath, NB extends SVGPathPeerBase<N, NB, NM>, NM extends SVGPathPeerMixin<N, NB, NM>>

        extends HtmlShapePeer<N, NB, NM>
        implements SVGPathPeerMixin<N, NB, NM>, HtmlLayoutMeasurableNoGrow {

    private final SvgPathPeer svgPathPeer = new SvgPathPeer();
    private final Element svgElement = SvgUtil.createSvgElement("svg");

    public HtmlSVGPathPeer() {
        this((NB) new SVGPathPeerBase(), HtmlUtil.createElement("fx-svgpath"));
    }

    public HtmlSVGPathPeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        super.bind(node, sceneRequester);
        SvgRoot svgRoot = new SvgRootBase();
        node.getProperties().put("svgRoot", svgRoot);
        svgPathPeer.getNodePeerBase().setNode(node); // Necessary, otherwise NPE when fill is a gradient
        HtmlUtil.setChildren(svgElement, svgRoot.getDefsElement(), svgPathPeer.getElement());
        HtmlUtil.appendChild(DomGlobal.document.body, svgElement);
        updateEffect(node.getEffect());
        updateFill(node.getFill());
        updateContent(node.getContent());
        HtmlUtil.setChild(getElement(), svgElement);
    }

    private boolean isBound() {
        return getNode().getProperties().containsKey("svgRoot");
    }

    private SVGRect bBox;

    private SVGRect getBBox() {
        if (bBox == null)
            bBox = svgPathPeer.getBBox();
        return bBox;
    }

    @Override
    public void updateEffect(Effect effect) {
        if (isBound())
            svgPathPeer.updateEffect(effect);
    }

    @Override
    public void updateFill(Paint fill) {
        if (isBound())
            svgPathPeer.updateFill(fill);
    }

    @Override
    public void updateStroke(Paint stroke) {
        if (isBound())
            svgPathPeer.updateStroke(stroke);
    }

    @Override
    public void updateStrokeWidth(Double strokeWidth) {
        svgPathPeer.updateStrokeWidth(strokeWidth);
    }

    @Override
    public void updateStrokeType(StrokeType strokeType) {
        svgPathPeer.updateStrokeType(strokeType);
    }

    @Override
    public void updateStrokeLineCap(StrokeLineCap strokeLineCap) {
        svgPathPeer.updateStrokeLineCap(strokeLineCap);
    }

    @Override
    public void updateStrokeLineJoin(StrokeLineJoin strokeLineJoin) {
        svgPathPeer.updateStrokeLineJoin(strokeLineJoin);
    }

    @Override
    public void updateStrokeMiterLimit(Double strokeMiterLimit) {
        svgPathPeer.updateStrokeMiterLimit(strokeMiterLimit);
    }

    @Override
    public void updateStrokeDashOffset(Double strokeDashOffset) {
        svgPathPeer.updateStrokeDashOffset(strokeDashOffset);
    }

    @Override
    public void updateStrokeDashArray(List<Double> dashArray) {
        svgPathPeer.updateStrokeDashArray(dashArray);
    }

    @Override
    public void updateFillRule(FillRule fillRule) {
        svgPathPeer.updateFillRule(fillRule);
    }

    @Override
    public void updateContent(String content) {
        svgPathPeer.updatePath(content);
        bBox = null;
        if (svgElement != null) {
            getBBox(); // Note: bBox doesn't include strokes
            svgElement.setAttribute("width",  bBox.width);
            svgElement.setAttribute("height", bBox.height);
            svgElement.setAttribute("viewBox", bBox.x + " " + bBox.y + " " + bBox.width + " " + bBox.height);
            svgElement.setAttribute("overflow", "visible"); // To avoid clipping the strokes
            CSSStyleDeclaration style = getElement().style;
            style.width  = CSSProperties.WidthUnionType.of( bBox.width  + "px");
            style.height = CSSProperties.HeightUnionType.of(bBox.height + "px");
            style.left   = bBox.x + "px";
            style.top    = bBox.y + "px";
        }
        clearCache();
        //cache.setCachedLayoutBounds(bBoxToBound(bBox));
    }

    @Override
    public void updateLocalToParentTransforms(List<Transform> localToParentTransforms) {
        double tx = getBBox().x, ty = bBox.y;
        if (tx != 0 || ty != 0) {
            List<Transform> forSvgTransforms = new ArrayList<>(localToParentTransforms.size() + 2);
            boolean allTranslate = true;
            forSvgTransforms.add(new Translate(-tx, -ty));
            for (Transform transform : localToParentTransforms) {
                if (!(transform instanceof Translate)) {
                    allTranslate = false;
                    if (transform instanceof Scale) {
                        Scale scale = (Scale) transform;
                        tx /= scale.getX();
                        ty /= scale.getY();
                    }/* else { // Commented as it is wrong. TODO: extract the correct scale factors from Affine transform
                        Affine affine = transform.toAffine();
                        tx /= affine.getMxx();
                        ty /= affine.getMyy();
                    }*/
                }
                forSvgTransforms.add(transform);
            }
            if (!allTranslate) {
                forSvgTransforms.add(new Translate(tx, ty));
                localToParentTransforms = forSvgTransforms;
            }

        }
        super.updateLocalToParentTransforms(localToParentTransforms);
    }

    private final HtmlLayoutCache cache = new HtmlLayoutCache();
    @Override
    public HtmlLayoutCache getCache() {
        return cache;
    }

    private static Bounds bBoxToBound(SVGRect bBox) {
        return bBox == null ? null : new BoundingBox(bBox.x, bBox.y, 0, bBox.width, bBox.height, 0);
    }

    @Override
    public Bounds measureLayoutBounds() {
        return bBoxToBound(getBBox());
    }

    @Override
    public double sizeAndMeasure(double value, boolean width) {
        return width ? getBBox().width : getBBox().height;
    }
}
