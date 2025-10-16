package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html;

import dev.webfx.kit.mapper.peers.javafxgraphics.base.SVGPathPeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.SVGPathPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.measurable.MeasurableCache;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.layoutmeasurable.HtmlMeasurableNoGrow;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.svg.SvgPathPeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlUtil;
import elemental2.dom.HTMLElement;
import elemental2.svg.SVGRect;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.SVGPath;

/**
 * @author Bruno Salmon
 */
public final class HtmlSVGPathPeer
        <N extends SVGPath, NB extends SVGPathPeerBase<N, NB, NM>, NM extends SVGPathPeerMixin<N, NB, NM>>

        extends HtmlSVGShapePeer<N, NB, NM>
        implements SVGPathPeerMixin<N, NB, NM>, HtmlMeasurableNoGrow {

    private final SvgPathPeer svgPathPeer = new SvgPathPeer();

    public HtmlSVGPathPeer() {
        this((NB) new SVGPathPeerBase(), HtmlUtil.createElement("fx-svgpath"));
    }

    public HtmlSVGPathPeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    SvgPathPeer getSvgShapePeer() {
        return svgPathPeer;
    }

    @Override
    void doInitialUpdate() {
        super.doInitialUpdate();
        updateContent(getNode().getContent());
    }

    @Override
    public void updateFillRule(FillRule fillRule) {
        svgPathPeer.updateFillRule(fillRule);
    }

    @Override
    public void updateContent(String content) {
        svgPathPeer.updatePath(content);
        clearCache();
        //cache.setCachedLayoutBounds(bBoxToBound(bBox));
    }

    private final MeasurableCache cache = new MeasurableCache();
    @Override
    public MeasurableCache getCache() {
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
    public double prepareAndMeasureElement(HTMLElement e, boolean measureWidth, double otherSizeValue) {
        return measureWidth ? getBBox().width : getBBox().height;
    }

}
