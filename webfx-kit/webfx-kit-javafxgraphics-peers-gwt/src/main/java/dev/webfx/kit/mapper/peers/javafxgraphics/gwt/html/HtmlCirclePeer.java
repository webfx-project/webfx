package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import elemental2.dom.CSSProperties;
import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.HTMLElement;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import javafx.scene.shape.Circle;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.CirclePeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.CirclePeerMixin;

/**
 * @author Bruno Salmon
 */
public final class HtmlCirclePeer
        <N extends Circle, NB extends CirclePeerBase<N, NB, NM>, NM extends CirclePeerMixin<N, NB, NM>>

        extends HtmlShapePeer<N, NB, NM>
        implements CirclePeerMixin<N, NB, NM> {

    public HtmlCirclePeer() {
        this((NB) new CirclePeerBase(), HtmlUtil.createElement("fx-circle"));
    }

    public HtmlCirclePeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    protected String computeClipPath() {
        Circle c = getNode();
        return "circle(" + toPx(c.getRadius()) + " at " + toPx(c.getCenterX()) + " " + toPx(c.getCenterY());
    }

    @Override
    public void updateCenterX(Double centerX) {
        if (isClip())
            applyClipPathToClipNodes();
        else
            getElement().style.left = (centerX - getNode().getRadius()) + "px";
    }

    @Override
    public void updateCenterY(Double centerY) {
        if (isClip())
            applyClipPathToClipNodes();
        else
            getElement().style.top = toPx(centerY - getNode().getRadius());
    }

    @Override
    public void updateRadius(Double radius) {
        if (isClip())
            applyClipPathToClipNodes();
        else {
            CSSStyleDeclaration style = getElement().style;
            String px = toPx(2 * radius);
            style.width = CSSProperties.WidthUnionType.of(px);
            style.height = CSSProperties.HeightUnionType.of(px);
            style.borderRadius = CSSProperties.BorderRadiusUnionType.of(toPx(radius));
            style.boxSizing = "border-box"; // We don't want the border to shift the circle position
            updateCenterX(getNode().getCenterX());
            updateCenterY(getNode().getCenterY());
        }
    }
}
