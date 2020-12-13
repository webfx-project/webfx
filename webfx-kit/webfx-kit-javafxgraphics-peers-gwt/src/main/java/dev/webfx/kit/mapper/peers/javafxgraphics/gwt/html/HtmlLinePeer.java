package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import elemental2.dom.CSSProperties;
import elemental2.dom.HTMLElement;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlPaints;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.LinePeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.LinePeerMixin;

/**
 * This temporary implementation works only for horizontal and vertical lines (not diagonals)
 * @author Bruno Salmon
 */
public final class HtmlLinePeer
        <N extends Line, NB extends LinePeerBase<N, NB, NM>, NM extends LinePeerMixin<N, NB, NM>>

        extends HtmlShapePeer<N, NB, NM>
        implements LinePeerMixin<N, NB, NM> {

    public HtmlLinePeer() {
        this((NB) new LinePeerBase(), HtmlUtil.createElement("fx-line"));
    }

    public HtmlLinePeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    public void updateStartX(Double startX) {
        updateElement();
    }

    @Override
    public void updateStartY(Double startY) {
        updateElement();
    }

    @Override
    public void updateEndX(Double endX) {
        updateElement();
    }

    @Override
    public void updateEndY(Double endY) {
        updateElement();
    }

    @Override
    public void updateStrokeWidth(Double strokeWidth) {
        super.updateStrokeWidth(strokeWidth);
        updateElement();
    }

    private void updateElement() {
        N n = getNode();
        Double startX = n.getStartX();
        Double endX = n.getEndX();
        Double startY = n.getStartY();
        Double endY = n.getEndY();
        HTMLElement e = getElement();
        e.style.left = toPx(Math.min(startX, endX) - n.getStrokeWidth() / 2);
        e.style.top = toPx(Math.min(startY, endY)  - n.getStrokeWidth() / 2);
        e.style.width = CSSProperties.WidthUnionType.of(toPx(Math.abs(endX - startX)));
        e.style.height = CSSProperties.HeightUnionType.of(toPx(Math.abs(endY - startY)));
    }

    @Override
    protected void updateStroke() {
        super.updateStroke();
        N shape = getNode();
        String color = HtmlPaints.toHtmlCssPaint(shape.getStroke());
        Double strokeWidth = shape.getStrokeWidth();
        boolean hasStroke = color != null && strokeWidth > 0;
        setElementStyleAttribute("border-width", hasStroke ? toPx(strokeWidth / 2) : null);
        setElementStyleAttribute("border-radius", hasStroke && shape.getStrokeLineCap() == StrokeLineCap.ROUND ? toPx(strokeWidth / 2) : null);
    }
}
