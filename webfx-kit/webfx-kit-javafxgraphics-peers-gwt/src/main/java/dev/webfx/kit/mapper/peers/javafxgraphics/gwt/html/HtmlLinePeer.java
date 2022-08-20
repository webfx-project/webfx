package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import dev.webfx.kit.mapper.peers.javafxgraphics.base.LinePeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.LinePeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.svg.SvgLinePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.svg.SvgShapePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import elemental2.dom.HTMLElement;
import javafx.scene.Cursor;
import javafx.scene.shape.Line;

/**
 * @author Bruno Salmon
 */
public final class HtmlLinePeer
        <N extends Line, NB extends LinePeerBase<N, NB, NM>, NM extends LinePeerMixin<N, NB, NM>>

        extends HtmlSVGShapePeer<N, NB, NM>
        implements LinePeerMixin<N, NB, NM> {

    private final SvgLinePeer<N, NB, NM> svgLinePeer = new SvgLinePeer<>();


    public HtmlLinePeer() {
        this((NB) new LinePeerBase(), HtmlUtil.createElement("fx-line"));
    }

    public HtmlLinePeer(NB base, HTMLElement element) {
        super(base, element);
        setElementStyleAttribute("pointer-events", "all");
    }

    @Override
    SvgShapePeer<N, NB, NM> getSvgShapePeer() {
        return svgLinePeer;
    }

    @Override
    void computeViewBox() {
        N node = getNode();
        x = y = 0;
        width = Math.abs(node.getEndX() - node.getStartX());
        height = Math.abs(node.getEndY() - node.getStartY());
        addExtraOnEffect();
        addExtraOnStrokeWidth();
    }

    @Override
    public void updateStartX(Double startX) {
        svgLinePeer.updateStartX(startX);
        updateViewBox();
    }

    @Override
    public void updateStartY(Double startY) {
        svgLinePeer.updateStartY(startY);
        updateViewBox();
    }

    @Override
    public void updateEndX(Double endX) {
        svgLinePeer.updateEndX(endX);
        updateViewBox();
    }

    @Override
    public void updateEndY(Double endY) {
        svgLinePeer.updateEndY(endY);
        updateViewBox();
    }

    @Override
    public void updateCursor(Cursor cursor) {
        svgLinePeer.updateCursor(cursor);
    }
}
