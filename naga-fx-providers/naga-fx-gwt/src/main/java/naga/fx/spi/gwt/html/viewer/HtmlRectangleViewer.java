package naga.fx.spi.gwt.html.viewer;

import elemental2.HTMLElement;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.scene.shape.Rectangle;
import naga.fx.spi.viewer.base.RectangleViewerBase;
import naga.fx.spi.viewer.base.RectangleViewerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlRectangleViewer
        <N extends Rectangle, NB extends RectangleViewerBase<N, NB, NM>, NM extends RectangleViewerMixin<N, NB, NM>>

        extends HtmlShapeViewer<N, NB, NM>
        implements RectangleViewerMixin<N, NB, NM> {

    public HtmlRectangleViewer() {
        this((NB) new RectangleViewerBase(), HtmlUtil.createDivElement());
    }

    public HtmlRectangleViewer(NB base, HTMLElement element) {
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
