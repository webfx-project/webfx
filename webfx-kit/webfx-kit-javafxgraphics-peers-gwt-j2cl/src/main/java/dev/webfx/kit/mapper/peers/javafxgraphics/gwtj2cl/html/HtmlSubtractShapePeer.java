package dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html;

import dev.webfx.kit.mapper.peers.javafxgraphics.base.ShapePeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.ShapePeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.svg.SvgCirclePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.svg.SvgRectanglePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.util.HtmlUtil;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.util.SvgUtil;
import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.SubtractShape;

/**
 *
 *
 * @author Bruno Salmon
 */
public final class HtmlSubtractShapePeer
        <N extends SubtractShape, NB extends ShapePeerBase<N, NB, NM>, NM extends ShapePeerMixin<N, NB, NM>>

        extends HtmlShapePeer<N, NB, NM>
        implements ShapePeerMixin<N, NB, NM> {

    public HtmlSubtractShapePeer() {
        this((NB) new ShapePeerBase(), HtmlUtil.createElement("fx-subtractshape"));
    }

    public HtmlSubtractShapePeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    public Element computeClipMask() {
        Element element1 = createSvgShape(getNode().getShape1());
        if (element1 == null)
            return null;
        Element element2 = createSvgShape(getNode().getShape2());
        if (element2 == null)
            return null;
        element1.setAttribute("fill", "white");
        element2.setAttribute("fill", "black");
        Element mask = SvgUtil.createSvgElement("mask");
        mask.append(element1, element2);
        return mask;
    }

    private Element createSvgShape(Shape shape) {
        if (shape instanceof Rectangle) {
            Rectangle r = (Rectangle) shape;
            SvgRectanglePeer svgPeer = new SvgRectanglePeer<>();
            svgPeer.updateX(r.getX());
            svgPeer.updateY(r.getY());
            svgPeer.updateWidth(r.getWidth());
            svgPeer.updateHeight(r.getHeight());
            return svgPeer.getElement();
        }
        if (shape instanceof Circle) {
            Circle c = (Circle) shape;
            SvgCirclePeer svgPeer = new SvgCirclePeer<>();
            svgPeer.updateCenterX(c.getCenterX());
            svgPeer.updateCenterY(c.getCenterY());
            svgPeer.updateRadius(c.getRadius());
            return svgPeer.getElement();
        }
        return null;
    }
}
