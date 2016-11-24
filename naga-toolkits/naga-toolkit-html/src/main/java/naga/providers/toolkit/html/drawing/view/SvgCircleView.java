package naga.providers.toolkit.html.drawing.view;

import naga.providers.toolkit.html.util.SvgUtil;
import naga.toolkit.drawing.shape.Circle;
import naga.toolkit.drawing.spi.view.base.CircleViewBase;
import naga.toolkit.drawing.spi.view.base.CircleViewMixin;

/**
 * @author Bruno Salmon
 */
public class SvgCircleView
        extends SvgShapeView<Circle, CircleViewBase, CircleViewMixin>
        implements CircleViewMixin {

    public SvgCircleView() {
        super(new CircleViewBase(), SvgUtil.createSvgCircle());
    }

    @Override
    public void updateCenterX(Double centerX) {
        setSvgAttribute("cx", centerX);
    }

    @Override
    public void updateCenterY(Double centerY) {
        setSvgAttribute("cy", centerY);
    }

    @Override
    public void updateRadius(Double radius) {
        setSvgAttribute("r", radius);
    }
}
