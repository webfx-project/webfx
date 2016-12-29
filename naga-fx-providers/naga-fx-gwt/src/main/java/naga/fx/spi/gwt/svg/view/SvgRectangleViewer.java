package naga.fx.spi.gwt.svg.view;

import elemental2.Element;
import naga.fx.spi.gwt.util.SvgUtil;
import naga.fx.scene.shape.Rectangle;
import naga.fx.spi.viewer.base.RectangleViewerBase;
import naga.fx.spi.viewer.base.RectangleViewerMixin;

/**
 * @author Bruno Salmon
 */
public class SvgRectangleViewer
        <N extends Rectangle, NB extends RectangleViewerBase<N, NB, NM>, NM extends RectangleViewerMixin<N, NB, NM>>

        extends SvgShapeViewer<N, NB, NM>
        implements RectangleViewerMixin<N, NB, NM> {

    public SvgRectangleViewer() {
        this((NB) new RectangleViewerBase(), SvgUtil.createSvgRectangle());
    }

    public SvgRectangleViewer(NB base, Element element) {
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
