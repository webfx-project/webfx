package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.TextPeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.TextPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.layoutmeasurable.HtmlLayoutMeasurableNoHGrow;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.shared.SvgRoot;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.shared.SvgRootBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.svg.SvgTextPeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.SvgUtil;
import elemental2.dom.*;
import elemental2.svg.SVGRect;
import javafx.geometry.VPos;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class HtmlSvgTextPeer
        <N extends Text, NB extends TextPeerBase<N, NB, NM>, NM extends TextPeerMixin<N, NB, NM>>
        extends HtmlShapePeer<N, NB, NM>
        implements TextPeerMixin<N, NB, NM>, HtmlLayoutMeasurableNoHGrow {

    private final Element svgElement = SvgUtil.createSvgElement("svg");
    private SvgTextPeer svgTextPeer = new SvgTextPeer();

    public HtmlSvgTextPeer() {
        this((NB) new TextPeerBase(), HtmlUtil.createElement("div"));
    }

    public HtmlSvgTextPeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        svgTextPeer.getNodePeerBase().setNode(node);
        SvgRoot svgRoot = new SvgRootBase();
        node.getProperties().put("svgRoot", svgRoot);
        HtmlUtil.setChildren(svgElement, svgRoot.getDefsElement(), svgTextPeer.getElement());
        HtmlUtil.appendChild(DomGlobal.document.body, svgElement);
        super.bind(node, sceneRequester);
        HtmlUtil.setChild(getElement(), svgElement);
    }

    private SVGRect bBox;

    private SVGRect getBBox() {
        //if (bBox == null)
        bBox = svgTextPeer.getBBox();
        return bBox;
    }

    @Override
    public void updateEffect(Effect effect) {
        svgTextPeer.updateEffect(effect);
        updateViewBox();
    }

    @Override
    public void updateFill(Paint fill) {
        svgTextPeer.updateFill(fill);
    }

    @Override
    public void updateStroke(Paint stroke) {
        svgTextPeer.updateStroke(stroke);
    }

    @Override
    public void updateStrokeWidth(Double strokeWidth) {
        svgTextPeer.updateStrokeWidth(strokeWidth);
    }

    @Override
    public void updateStrokeType(StrokeType strokeType) {
        svgTextPeer.updateStrokeType(strokeType);
    }

    @Override
    public void updateStrokeLineCap(StrokeLineCap strokeLineCap) {
        svgTextPeer.updateStrokeLineCap(strokeLineCap);
    }

    @Override
    public void updateStrokeLineJoin(StrokeLineJoin strokeLineJoin) {
        svgTextPeer.updateStrokeLineJoin(strokeLineJoin);
    }

    @Override
    public void updateStrokeMiterLimit(Double strokeMiterLimit) {
        svgTextPeer.updateStrokeMiterLimit(strokeMiterLimit);
    }

    @Override
    public void updateStrokeDashOffset(Double strokeDashOffset) {
        svgTextPeer.updateStrokeDashOffset(strokeDashOffset);
    }

    @Override
    public void updateStrokeDashArray(List<Double> dashArray) {
        svgTextPeer.updateStrokeDashArray(dashArray);
    }

    @Override
    public double measure(HTMLElement e, boolean width) {
        return width ? getBBox().width : getBBox().height;
    }

/*
    private final HtmlLayoutCache cache = new HtmlLayoutCache();
    @Override
    public HtmlLayoutCache getCache() {
        return cache;
    }
*/

    private void updateViewBox() {
        SVGRect bb = getBBox(); // Note: bBox doesn't include strokes, nor effect (drop shadow, etc...)
        double width = bb.width, height = bb.height, x = bb.x, y = bb.y;
        // Adding extra space if there is an effect to prevent it to be clipped
        if (getNode().getEffect() != null) {
            // Assuming 20px will be enough - TODO: Make an accurate computation
            width += 20;
            height += 20;
        }
        svgElement.setAttribute("width", width);
        svgElement.setAttribute("height", height);
        svgElement.setAttribute("viewBox", x + " " + y + " " + width + " " + height);
        svgElement.setAttribute("overflow", "visible"); // To avoid clipping the strokes (but may clip shadow, that's why we added margin in the viewBox)
    }

    @Override
    public void updateText(String text) {
        svgTextPeer.updateText(text);
        updateViewBox();
    }

    @Override
    public void updateTextOrigin(VPos textOrigin) {
        svgTextPeer.updateTextOrigin(textOrigin);
    }

    @Override
    public void updateX(Double x) {
        svgTextPeer.updateX(x);
    }

    @Override
    public void updateY(Double y) {
        svgTextPeer.updateY(y);
    }

    @Override
    public void updateWrappingWidth(Double wrappingWidth) {
        svgTextPeer.updateWrappingWidth(wrappingWidth);
    }

    @Override
    public void updateLineSpacing(Number lineSpacing) {
        svgTextPeer.updateLineSpacing(lineSpacing);
        updateViewBox();
    }

    @Override
    public void updateTextAlignment(TextAlignment textAlignment) {
        svgTextPeer.updateTextAlignment(textAlignment);
    }

    @Override
    public void updateFont(Font font) {
        svgTextPeer.updateFont(font);
        updateViewBox();
    }
}
