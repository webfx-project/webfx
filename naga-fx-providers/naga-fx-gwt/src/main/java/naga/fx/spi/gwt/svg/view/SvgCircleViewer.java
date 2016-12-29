package naga.fx.spi.gwt.svg.view;

import elemental2.Element;
import naga.fx.spi.gwt.util.SvgUtil;
import naga.fx.scene.shape.Circle;
import naga.fx.spi.viewer.base.CircleViewerBase;
import naga.fx.spi.viewer.base.CircleViewerMixin;

/**
 * @author Bruno Salmon
 */
public class SvgCircleViewer
        <N extends Circle, NB extends CircleViewerBase<N, NB, NM>, NM extends CircleViewerMixin<N, NB, NM>>

        extends SvgShapeViewer<N, NB, NM>
        implements CircleViewerMixin<N, NB, NM> {

    public SvgCircleViewer() {
        this((NB) new CircleViewerBase(), SvgUtil.createSvgCircle());
    }

    public SvgCircleViewer(NB base, Element element) {
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
