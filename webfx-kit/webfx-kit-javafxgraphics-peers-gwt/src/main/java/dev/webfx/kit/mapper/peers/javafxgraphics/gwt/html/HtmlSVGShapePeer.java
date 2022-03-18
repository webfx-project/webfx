package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.ShapePeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.ShapePeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.shared.SvgRoot;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.shared.SvgRootBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.svg.SvgShapePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.SvgUtil;
import elemental2.dom.CSSProperties;
import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLElement;
import elemental2.svg.SVGElement;
import elemental2.svg.SVGRect;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public abstract class HtmlSVGShapePeer
        <N extends Shape, NB extends ShapePeerBase<N, NB, NM>, NM extends ShapePeerMixin<N, NB, NM>>

        extends HtmlShapePeer<N, NB, NM>
        implements ShapePeerMixin<N, NB, NM> {

    protected final SVGElement svgElement = (SVGElement) SvgUtil.createSvgElement("svg");
    // Fields used for the position and size computation of the SVG viewBox
    protected double x, y, width, height;

    public HtmlSVGShapePeer(NB base, HTMLElement element) {
        super(base, element);
        setElementStyleAttribute("display", "flex"); // to remove undesired space between parent html element and child svg element
    }

    abstract SvgShapePeer<N, NB, NM> getSvgShapePeer();

    void doInitialUpdate() {
        N node = getNode();
        updateEffect(node.getEffect());
        updateFill(node.getFill());
    }

    void computeViewBox() {
        getBBox(); // Note: bBox doesn't include strokes
        x = bBox.x;
        y = bBox.y;
        width = bBox.width;
        height = bBox.height;
    }

    void addExtraOnEffect() {
        // Adding extra space if there is an effect to prevent it to be clipped
        if (getNode().getEffect() != null) {
            // Assuming 20px will be enough - TODO: Make an accurate computation
            width += 20;
            height += 20;
        }
    }

    void addExtraOnStrokeWidth() {
        // Adding extra space if there is a stroke
        N node = getNode();
        double strokeWidth = node.getStrokeWidth();
        width += strokeWidth;
        height += strokeWidth;
    }

    void updateViewBox() {
        bBox = null;
        if (svgElement != null) {
            computeViewBox();
            svgElement.setAttribute("width", width);
            svgElement.setAttribute("height", height);
            svgElement.setAttribute("viewBox", x + " " + y + " " + width + " " + height);
            svgElement.setAttribute("overflow", "visible"); // To avoid clipping the strokes
            CSSStyleDeclaration style = getElement().style;
            style.width  = CSSProperties.WidthUnionType.of(width + "px");
            style.height = CSSProperties.HeightUnionType.of(height + "px");
            style.left   = x + "px";
            style.top    = y + "px";
        }
    }
        @Override
    public void bind(N node, SceneRequester sceneRequester) {
        super.bind(node, sceneRequester);
        SvgRoot svgRoot = new SvgRootBase();
        node.getProperties().put("svgRoot", svgRoot);
        SvgShapePeer<N, NB, NM> svgShapePeer = getSvgShapePeer();
        svgShapePeer.getNodePeerBase().setNode(node); // Necessary, otherwise NPE when fill is a gradient
        HtmlUtil.setChildren(svgElement, svgRoot.getDefsElement(), svgShapePeer.getElement());
        HtmlUtil.appendChild(DomGlobal.document.body, svgElement);
        doInitialUpdate();
        updateViewBox();
        HtmlUtil.setChild(getElement(), svgElement);
    }

    private boolean isBound() {
        return getNode().getProperties().containsKey("svgRoot");
    }

    protected SVGRect bBox;

    protected SVGRect getBBox() {
        if (bBox == null) {
            bBox = getSvgShapePeer().getBBox();
            if (bBox.width == 0) {
                SVGRect viewBox = getViewBox(svgElement);
                if (viewBox != null) // null value happens on FireFox
                    bBox = viewBox;
            }
        }
        return bBox;
    }

    private static native SVGRect getViewBox(SVGElement svgElement) /*-{
        return svgElement.viewBox.baseVal;
    }-*/;

    @Override
    public void updateEffect(Effect effect) {
        if (isBound())
            getSvgShapePeer().updateEffect(effect);
    }

    @Override
    public void updateFill(Paint fill) {
        if (isBound())
            getSvgShapePeer().updateFill(fill);
    }

    @Override
    public void updateStroke(Paint stroke) {
        if (isBound())
            getSvgShapePeer().updateStroke(stroke);
    }

    @Override
    public void updateStrokeWidth(Double strokeWidth) {
        getSvgShapePeer().updateStrokeWidth(strokeWidth);
    }

    @Override
    public void updateStrokeType(StrokeType strokeType) {
        getSvgShapePeer().updateStrokeType(strokeType);
    }

    @Override
    public void updateStrokeLineCap(StrokeLineCap strokeLineCap) {
        getSvgShapePeer().updateStrokeLineCap(strokeLineCap);
    }

    @Override
    public void updateStrokeLineJoin(StrokeLineJoin strokeLineJoin) {
        getSvgShapePeer().updateStrokeLineJoin(strokeLineJoin);
    }

    @Override
    public void updateStrokeMiterLimit(Double strokeMiterLimit) {
        getSvgShapePeer().updateStrokeMiterLimit(strokeMiterLimit);
    }

    @Override
    public void updateStrokeDashOffset(Double strokeDashOffset) {
        getSvgShapePeer().updateStrokeDashOffset(strokeDashOffset);
    }

    @Override
    public void updateStrokeDashArray(List<Double> dashArray) {
        getSvgShapePeer().updateStrokeDashArray(dashArray);
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

}
