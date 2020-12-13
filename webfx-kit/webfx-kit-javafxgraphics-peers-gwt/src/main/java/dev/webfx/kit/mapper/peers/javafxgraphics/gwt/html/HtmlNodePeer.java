package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import elemental2.dom.CSSProperties;
import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.*;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Transform;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.NodePeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.NodePeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.shared.HtmlSvgNodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlPaints;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlTransforms;
import dev.webfx.platform.shared.util.Strings;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public abstract class HtmlNodePeer
        <N extends Node, NB extends NodePeerBase<N, NB, NM>, NM extends NodePeerMixin<N, NB, NM>>

        extends HtmlSvgNodePeer<HTMLElement, N, NB, NM> {

    HtmlNodePeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    public void updateLocalToParentTransforms(List<Transform> localToParentTransforms) {
        Element container = getVisibleContainer();
        if (!(container instanceof HTMLElement))
            super.updateLocalToParentTransforms(localToParentTransforms);
        else {
            String transform = HtmlTransforms.toHtmlTransforms(localToParentTransforms);
            CSSStyleDeclaration style = ((HTMLElement) container).style;
            style.transform = transform;
            if (Strings.contains(transform, "matrix"))
                style.transformOrigin = CSSProperties.TransformOriginUnionType.of("0px 0px 0px");
            else if (Strings.contains(transform,"scale"))
                style.transformOrigin = CSSProperties.TransformOriginUnionType.of("center");
        }
    }

    @Override
    public void updateEffect(Effect effect) {
        String boxShadow = toBoxShadow(effect);
        CSSStyleDeclaration style = getElement().style;
        style.boxShadow = boxShadow;
        super.updateEffect(boxShadow != null ? null : effect); // other effects managed by filter function
    }

    private String toBoxShadow(Effect effect) {
        String boxShadow = null;
        if (effect instanceof InnerShadow) {
            InnerShadow innerShadow = (InnerShadow) effect;
            boxShadow = innerShadow.getOffsetX() + "px " + innerShadow.getOffsetY() + "px " + innerShadow.getRadius() + "px " + innerShadow.getChoke() + "px " + HtmlPaints.toCssColor(innerShadow.getColor()) + " inset";
            if (innerShadow.getInput() != null)
                boxShadow = toBoxShadow(innerShadow.getInput()) + ", " + boxShadow;
        } else if (effect instanceof DropShadow) {
            DropShadow dropShadow = (DropShadow) effect;
            boxShadow = dropShadow.getOffsetX() + "px " + dropShadow.getOffsetY() + "px " + dropShadow.getRadius() + "px " + HtmlPaints.toCssColor(dropShadow.getColor());
            if (dropShadow.getInput() != null)
                boxShadow = toBoxShadow(dropShadow.getInput()) + ", " + boxShadow;
        }
        return boxShadow;
    }

    @Override
    protected String toFilter(Effect effect) {
        if (effect == null)
            return null;
        if (effect instanceof GaussianBlur)
            return "blur(" + ((GaussianBlur) effect).getSigma() + "px)";
        if (effect instanceof BoxBlur)
            // Not supported by browser so doing a gaussian blur instead
            return "blur(" + GaussianBlur.getSigma(((BoxBlur) effect).getWidth()) + "px)";
        if (effect instanceof DropShadow) {
            DropShadow dropShadow = (DropShadow) effect;
            return "drop-shadow(" + toPx(dropShadow.getOffsetX()) + " " + toPx(dropShadow.getOffsetY()) + " " + toPx(dropShadow.getRadius() / 2) + " " + HtmlPaints.toCssColor(dropShadow.getColor()) + ")";
        }
        return null;
    }

    public static String toCssTextAlignment(TextAlignment textAlignment) {
        if (textAlignment != null)
            switch (textAlignment) {
                case LEFT: return "left";
                case CENTER: return "center";
                case RIGHT: return "right";
            }
        return null;
    }

    public static String toCssTextAlignment(Pos pos) {
        return pos == null ? null : toCssTextAlignment(pos.getHpos());
    }

    static String toCssTextAlignment(HPos hPos) {
        if (hPos != null)
            switch (hPos) {
                case LEFT: return "left";
                case CENTER: return "center";
                case RIGHT: return "right";
            }
        return null;
    }

    public static String toPx(double position) {
        return position + "px";
    }

}
