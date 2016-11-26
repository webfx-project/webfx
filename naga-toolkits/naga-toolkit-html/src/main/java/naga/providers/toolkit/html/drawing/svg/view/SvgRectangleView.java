package naga.providers.toolkit.html.drawing.svg.view;

import naga.providers.toolkit.html.util.SvgUtil;
import naga.toolkit.drawing.shape.Rectangle;
import naga.toolkit.drawing.spi.view.base.RectangleViewBase;
import naga.toolkit.drawing.spi.view.base.RectangleViewMixin;

/**
 * @author Bruno Salmon
 */
public class SvgRectangleView
        extends SvgShapeView<Rectangle, RectangleViewBase, RectangleViewMixin>
        implements RectangleViewMixin {

    public SvgRectangleView() {
        super(new RectangleViewBase(), SvgUtil.createSvgRectangle());
    }

    @Override
    public void updateX(Double x) {
        setElementAttribute("x", x);
    }

    @Override
    public void updateY(Double y) {
        setElementAttribute("y", y);
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
        setElementAttribute("rx", arcWidth == null ? null : arcWidth / 2);
    }

    @Override
    public void updateArcHeight(Double arcHeight) {
        setElementAttribute("ry", arcHeight == null ? null : arcHeight / 2);
    }
}
