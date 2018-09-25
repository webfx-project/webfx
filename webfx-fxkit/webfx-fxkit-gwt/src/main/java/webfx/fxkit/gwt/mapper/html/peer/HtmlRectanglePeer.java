package webfx.fxkit.gwt.mapper.html.peer;

import elemental2.dom.CSSProperties;
import elemental2.dom.HTMLElement;
import webfx.fxkit.gwt.mapper.util.HtmlUtil;
import emul.javafx.scene.shape.Rectangle;
import webfx.fxkits.core.mapper.spi.impl.peer.RectanglePeerBase;
import webfx.fxkits.core.mapper.spi.impl.peer.RectanglePeerMixin;

/**
 * @author Bruno Salmon
 */
public final class HtmlRectanglePeer
        <N extends Rectangle, NB extends RectanglePeerBase<N, NB, NM>, NM extends RectanglePeerMixin<N, NB, NM>>

        extends HtmlShapePeer<N, NB, NM>
        implements RectanglePeerMixin<N, NB, NM> {

    public HtmlRectanglePeer() {
        this((NB) new RectanglePeerBase(), HtmlUtil.createDivElement());
    }

    public HtmlRectanglePeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    public void updateX(Double x) {
        getElement().style.left = toPx(x);
    }

    @Override
    public void updateY(Double y) {
        getElement().style.top = toPx(y);
    }

    @Override
    public void updateWidth(Double width) {
        getElement().style.width = CSSProperties.WidthUnionType.of(toPx(width));
    }

    @Override
    public void updateHeight(Double height) {
        getElement().style.height = CSSProperties.HeightUnionType.of(toPx(height));
    }

    @Override
    public void updateArcWidth(Double arcWidth) {
        updateBorderRadius();
    }

    @Override
    public void updateArcHeight(Double arcHeight) {
        updateBorderRadius();
    }

    private void updateBorderRadius() {
        Rectangle r = getNode();
        getElement().style.borderRadius = CSSProperties.BorderRadiusUnionType.of(toPx(r.getArcWidth()/2) + " " + toPx(r.getArcHeight()/2));
    }
}
