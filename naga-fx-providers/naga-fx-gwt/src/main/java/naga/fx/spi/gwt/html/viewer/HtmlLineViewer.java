package naga.fx.spi.gwt.html.viewer;

import elemental2.HTMLElement;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.scene.shape.Line;
import naga.fx.spi.viewer.base.LineViewerBase;
import naga.fx.spi.viewer.base.LineViewerMixin;

/**
 * This temporary implementation works only for horizontal and vertical lines (not diagonals)
 * @author Bruno Salmon
 */
public class HtmlLineViewer
        <N extends Line, NB extends LineViewerBase<N, NB, NM>, NM extends LineViewerMixin<N, NB, NM>>

        extends HtmlShapeViewer<N, NB, NM>
        implements LineViewerMixin<N, NB, NM> {

    public HtmlLineViewer() {
        this((NB) new LineViewerBase(), HtmlUtil.createDivElement());
    }

    public HtmlLineViewer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    public void updateStartX(Double startX) {
        updateElement();
    }

    @Override
    public void updateStartY(Double startY) {
        updateElement();
    }

    @Override
    public void updateEndX(Double endX) {
        updateElement();
    }

    @Override
    public void updateEndY(Double endY) {
        updateElement();
    }

    private void updateElement() {
        N n = getNode();
        Double startX = n.getStartX();
        Double endX = n.getEndX();
        Double startY = n.getStartY();
        Double endY = n.getEndY();
        HTMLElement e = getElement();
        e.style.left = toPx(Math.min(startX, endX));
        e.style.top = toPx(Math.min(startY, endY));
        e.style.width = toPx(Math.abs(endX - startX));
        e.style.height = toPx(Math.abs(endY - startY));
    }
}
