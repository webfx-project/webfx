package naga.providers.toolkit.html.fx.html.viewer;

import elemental2.HTMLElement;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.fx.scene.shape.Rectangle;
import naga.toolkit.fx.spi.viewer.base.RectangleViewerBase;
import naga.toolkit.fx.spi.viewer.base.RectangleViewerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlRectangleViewer
        <N extends Rectangle, NV extends RectangleViewerBase<N, NV, NM>, NM extends RectangleViewerMixin<N, NV, NM>>

        extends HtmlShapeViewer<N, NV, NM>
        implements RectangleViewerMixin<N, NV, NM> {

    public HtmlRectangleViewer() {
        this((NV) new RectangleViewerBase(), HtmlUtil.createDivElement());
    }

    public HtmlRectangleViewer(NV base, HTMLElement element) {
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
