package naga.providers.toolkit.html.drawing.html.view;

import elemental2.CSSStyleDeclaration;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.drawing.shape.Circle;
import naga.toolkit.drawing.spi.view.base.CircleViewBase;
import naga.toolkit.drawing.spi.view.base.CircleViewMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlCircleView
        extends HtmlShapeView<Circle, CircleViewBase, CircleViewMixin>
        implements CircleViewMixin {

    public HtmlCircleView() {
        super(new CircleViewBase(), HtmlUtil.createDivElement());
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
