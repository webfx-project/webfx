package webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import elemental2.dom.CSSProperties;
import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.HTMLElement;
import webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import javafx.scene.shape.Circle;
import webfx.kit.mapper.peers.javafxgraphics.base.CirclePeerBase;
import webfx.kit.mapper.peers.javafxgraphics.base.CirclePeerMixin;

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
    public void updateCenterX(Double centerX) {
        getElement().style.left = (centerX - getNode().getRadius()) + "px";
    }

    @Override
    public void updateCenterY(Double centerY) {
        getElement().style.top = toPx(centerY - getNode().getRadius());
    }

    @Override
    public void updateRadius(Double radius) {
        CSSStyleDeclaration style = getElement().style;
        String px = toPx(2 * radius);
        style.width = CSSProperties.WidthUnionType.of(px);
        style.height = CSSProperties.HeightUnionType.of(px);
        style.borderRadius = CSSProperties.BorderRadiusUnionType.of(toPx(radius));
    }
}
