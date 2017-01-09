package naga.fx.spi.gwt.html.peer;

import elemental2.CSSStyleDeclaration;
import elemental2.HTMLElement;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.scene.shape.Circle;
import naga.fx.spi.peer.base.CirclePeerBase;
import naga.fx.spi.peer.base.CirclePeerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlCirclePeer
        <N extends Circle, NB extends CirclePeerBase<N, NB, NM>, NM extends CirclePeerMixin<N, NB, NM>>

        extends HtmlShapePeer<N, NB, NM>
        implements CirclePeerMixin<N, NB, NM> {

    public HtmlCirclePeer() {
        this((NB) new CirclePeerBase(), HtmlUtil.createDivElement());
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
        style.width = style.height = toPx(2*radius);
        style.borderRadius = toPx(radius);
    }
}
