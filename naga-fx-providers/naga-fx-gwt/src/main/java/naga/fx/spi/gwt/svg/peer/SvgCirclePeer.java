package naga.fx.spi.gwt.svg.peer;

import elemental2.Element;
import naga.fx.spi.gwt.util.SvgUtil;
import naga.fx.scene.shape.Circle;
import naga.fx.spi.peer.base.CirclePeerBase;
import naga.fx.spi.peer.base.CirclePeerMixin;

/**
 * @author Bruno Salmon
 */
public class SvgCirclePeer
        <N extends Circle, NB extends CirclePeerBase<N, NB, NM>, NM extends CirclePeerMixin<N, NB, NM>>

        extends SvgShapePeer<N, NB, NM>
        implements CirclePeerMixin<N, NB, NM> {

    public SvgCirclePeer() {
        this((NB) new CirclePeerBase(), SvgUtil.createSvgCircle());
    }

    public SvgCirclePeer(NB base, Element element) {
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
