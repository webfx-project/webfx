package naga.providers.toolkit.html.drawing.view;

import naga.providers.toolkit.html.util.SvgUtil;
import naga.toolkit.drawing.shapes.Rectangle;
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
        setSvgAttribute("x", x);
    }

    @Override
    public void updateY(Double y) {
        setSvgAttribute("y", y);
    }

    @Override
    public void updateWidth(Double width) {
        setSvgAttribute("width", width);
    }

    @Override
    public void updateHeight(Double height) {
        setSvgAttribute("height", height);
    }

    @Override
    public void updateArcWidth(Double arcWidth) {
        setSvgAttribute("rx", arcWidth == null ? null : arcWidth / 2);
    }

    @Override
    public void updateArcHeight(Double arcHeight) {
        setSvgAttribute("ry", arcHeight == null ? null : arcHeight / 2);
    }
}
