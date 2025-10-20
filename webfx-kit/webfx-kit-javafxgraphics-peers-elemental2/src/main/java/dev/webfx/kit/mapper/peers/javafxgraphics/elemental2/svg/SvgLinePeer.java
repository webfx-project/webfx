package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.svg;

import dev.webfx.kit.mapper.peers.javafxgraphics.base.LinePeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.LinePeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.SvgUtil;
import elemental2.dom.Element;
import javafx.scene.shape.Line;

/**
 * @author Bruno Salmon
 */
public final class SvgLinePeer
        <N extends Line, NB extends LinePeerBase<N, NB, NM>, NM extends LinePeerMixin<N, NB, NM>>

        extends SvgShapePeer<N, NB, NM>
        implements LinePeerMixin<N, NB, NM> {

    public SvgLinePeer() {
        this((NB) new LinePeerBase(), SvgUtil.createSvgLine());
    }

    public SvgLinePeer(NB base, Element element) {
        super(base, element);
    }

    @Override
    public void updateStartX(Double startX) {
        setElementAttribute("x1", startX, 0d);
    }

    @Override
    public void updateStartY(Double startY) {
        setElementAttribute("y1", startY, 0d);
    }

    @Override
    public void updateEndX(Double endX) {
        setElementAttribute("x2", endX, 0d);
    }

    @Override
    public void updateEndY(Double endY) {
        setElementAttribute("y2", endY, 0d);
    }
}
