package naga.providers.toolkit.html.fx.svg.view;

import elemental2.Element;
import naga.providers.toolkit.html.util.SvgUtil;
import naga.toolkit.fx.scene.shape.Circle;
import naga.toolkit.fx.spi.viewer.base.CircleViewerBase;
import naga.toolkit.fx.spi.viewer.base.CircleViewerMixin;

/**
 * @author Bruno Salmon
 */
public class SvgCircleViewer
        <N extends Circle, NV extends CircleViewerBase<N, NV, NM>, NM extends CircleViewerMixin<N, NV, NM>>

        extends SvgShapeViewer<N, NV, NM>
        implements CircleViewerMixin<N, NV, NM> {

    public SvgCircleViewer() {
        this((NV) new CircleViewerBase(), SvgUtil.createSvgCircle());
    }

    public SvgCircleViewer(NV base, Element element) {
        super(base, element);
    }

    @Override
    public void updateCenterX(Double centerX) {
        setElementAttribute("cx", centerX);
    }

    @Override
    public void updateCenterY(Double centerY) {
        setElementAttribute("cy", centerY);
    }

    @Override
    public void updateRadius(Double radius) {
        setElementAttribute("r", radius);
    }
}
