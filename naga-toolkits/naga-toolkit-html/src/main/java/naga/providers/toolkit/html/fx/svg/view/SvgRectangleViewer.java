package naga.providers.toolkit.html.fx.svg.view;

import elemental2.Element;
import naga.providers.toolkit.html.util.SvgUtil;
import naga.toolkit.fx.scene.shape.Rectangle;
import naga.toolkit.fx.spi.viewer.base.RectangleViewerBase;
import naga.toolkit.fx.spi.viewer.base.RectangleViewerMixin;

/**
 * @author Bruno Salmon
 */
public class SvgRectangleViewer
        <N extends Rectangle, NV extends RectangleViewerBase<N, NV, NM>, NM extends RectangleViewerMixin<N, NV, NM>>

        extends SvgShapeViewer<N, NV, NM>
        implements RectangleViewerMixin<N, NV, NM> {

    public SvgRectangleViewer() {
        this((NV) new RectangleViewerBase(), SvgUtil.createSvgRectangle());
    }

    public SvgRectangleViewer(NV base, Element element) {
        super(base, element);
    }

    @Override
    public void updateX(Double x) {
        setElementAttribute("x", x, 0d);
    }

    @Override
    public void updateY(Double y) {
        setElementAttribute("y", y, 0d);
    }

    @Override
    public void updateWidth(Double width) {
        setElementAttribute("width", width);
    }

    @Override
    public void updateHeight(Double height) {
        setElementAttribute("height", height);
    }

    @Override
    public void updateArcWidth(Double arcWidth) {
        setElementAttribute("rx", arcWidth == null ? null : arcWidth / 2, 0d);
    }

    @Override
    public void updateArcHeight(Double arcHeight) {
        setElementAttribute("ry", arcHeight == null ? null : arcHeight / 2, 0d);
    }
}
