package naga.providers.toolkit.html.drawing.html.view;

import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.drawing.shape.Rectangle;
import naga.toolkit.drawing.spi.view.base.RectangleViewBase;
import naga.toolkit.drawing.spi.view.base.RectangleViewMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlRectangleView
        extends HtmlShapeView<Rectangle, RectangleViewBase, RectangleViewMixin>
        implements RectangleViewMixin {

    public HtmlRectangleView() {
        super(new RectangleViewBase(), HtmlUtil.createAbsolutePositionDiv());
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
