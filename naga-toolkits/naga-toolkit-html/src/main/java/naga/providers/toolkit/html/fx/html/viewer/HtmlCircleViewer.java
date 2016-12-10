package naga.providers.toolkit.html.fx.html.viewer;

import elemental2.CSSStyleDeclaration;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.fx.scene.shape.Circle;
import naga.toolkit.fx.spi.viewer.base.CircleViewerBase;
import naga.toolkit.fx.spi.viewer.base.CircleViewerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlCircleViewer
        extends HtmlShapeViewer<Circle, CircleViewerBase, CircleViewerMixin>
        implements CircleViewerMixin {

    public HtmlCircleViewer() {
        super(new CircleViewerBase(), HtmlUtil.createDivElement());
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
