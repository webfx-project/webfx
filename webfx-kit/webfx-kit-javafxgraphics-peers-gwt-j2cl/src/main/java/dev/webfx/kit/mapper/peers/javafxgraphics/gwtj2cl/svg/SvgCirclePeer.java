package dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.svg;

import elemental2.dom.Element;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.util.SvgUtil;
import javafx.scene.shape.Circle;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.CirclePeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.CirclePeerMixin;

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
