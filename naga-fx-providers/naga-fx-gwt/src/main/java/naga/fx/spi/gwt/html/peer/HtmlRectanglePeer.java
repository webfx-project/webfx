package naga.fx.spi.gwt.html.peer;

import elemental2.HTMLElement;
import naga.fx.spi.gwt.util.HtmlUtil;
import emul.javafx.scene.shape.Rectangle;
import naga.fx.spi.peer.base.RectanglePeerBase;
import naga.fx.spi.peer.base.RectanglePeerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlRectanglePeer
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
        getElement().style.width = toPx(width);
    }

    @Override
    public void updateHeight(Double height) {
        getElement().style.height = toPx(height);
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
        getElement().style.borderRadius = toPx(r.getArcWidth()/2) + " " + toPx(r.getArcHeight()/2);
    }
}
