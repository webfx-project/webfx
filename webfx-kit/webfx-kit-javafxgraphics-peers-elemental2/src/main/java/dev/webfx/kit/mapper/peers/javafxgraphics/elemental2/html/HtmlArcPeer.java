package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html;

import elemental2.dom.HTMLCanvasElement;
import elemental2.dom.HTMLElement;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.ArcPeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.ArcPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlUtil;

/**
 * @author Bruno Salmon
 */
public final class HtmlArcPeer
        <N extends Arc, NB extends ArcPeerBase<N, NB, NM>, NM extends ArcPeerMixin<N, NB, NM>>

        extends HtmlShapePeer<N, NB, NM>
        implements ArcPeerMixin<N, NB, NM> {

    private final HTMLCanvasElement canvasElement;
    private final HtmlGraphicsContext ctx;

    public HtmlArcPeer() {
        this((NB) new ArcPeerBase(), HtmlUtil.createElement("canvas"));
    }

    public HtmlArcPeer(NB base, HTMLElement element) {
        super(base, element);
        canvasElement = (HTMLCanvasElement) element;
        ctx = new HtmlGraphicsContext(canvasElement);
    }

    @Override
    public void updateFill(Paint fill) {
        //ctx.setFill(fill);
        updateCanvas();
    }

    @Override
    public void updateType(ArcType arcType) {
        updateCanvas();
    }

    @Override
    public void updateCenterX(Double centerX) {
        updateCanvas();
    }

    @Override
    public void updateCenterY(Double centerY) {
        updateCanvas();
    }

    @Override
    public void updateRadiusX(Double radiusX) {
        updateCanvas();
    }

    @Override
    public void updateRadiusY(Double radiusY) {
        updateCanvas();
    }

    @Override
    public void updateStartAngle(Double startAngle) {
        updateCanvas();
    }

    @Override
    public void updateLength(Double length) {
        updateCanvas();
    }

    private void updateCanvas() {
        HTMLCanvasElement canvas = (HTMLCanvasElement) getElement();
        N a = getNode();
        double width =  a.getCenterX() + a.getRadiusX();
        double height = a.getCenterY() + a.getRadiusY();
        setElementAttribute(canvas, "width", toPx(width));
        setElementAttribute(canvas, "height", toPx(height));
        ctx.clearRect(0, 0, width, height);
        ctx.setFill(a.getFill());
        ctx.fillArc(a.getCenterX() - a.getRadiusX(), a.getCenterY() - a.getRadiusY(), 2 * a.getRadiusX(), 2 * a.getRadiusY(), a.getStartAngle(), a.getLength(), a.getType());
    }
}
