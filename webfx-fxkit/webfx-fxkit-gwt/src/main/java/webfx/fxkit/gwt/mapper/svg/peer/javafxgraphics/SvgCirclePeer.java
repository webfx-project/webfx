package webfx.fxkit.gwt.mapper.svg.peer.javafxgraphics;

import elemental2.dom.Element;
import webfx.fxkit.gwt.mapper.util.SvgUtil;
import javafx.scene.shape.Circle;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.base.CirclePeerBase;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.base.CirclePeerMixin;

/**
 * @author Bruno Salmon
 */
public final class SvgCirclePeer
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
