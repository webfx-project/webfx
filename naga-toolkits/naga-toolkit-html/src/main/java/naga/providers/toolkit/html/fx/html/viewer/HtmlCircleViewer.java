package naga.providers.toolkit.html.fx.html.viewer;

import elemental2.CSSStyleDeclaration;
import elemental2.HTMLElement;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.fx.scene.shape.Circle;
import naga.toolkit.fx.spi.viewer.base.CircleViewerBase;
import naga.toolkit.fx.spi.viewer.base.CircleViewerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlCircleViewer
        <N extends Circle, NV extends CircleViewerBase<N, NV, NM>, NM extends CircleViewerMixin<N, NV, NM>>

        extends HtmlShapeViewer<N, NV, NM>
        implements CircleViewerMixin<N, NV, NM> {

    public HtmlCircleViewer() {
        this((NV) new CircleViewerBase(), HtmlUtil.createDivElement());
    }

    public HtmlCircleViewer(NV base, HTMLElement element) {
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
