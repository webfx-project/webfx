package webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import elemental2.dom.CSSProperties;
import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Transform;
import webfx.kit.mapper.peers.javafxgraphics.base.NodePeerBase;
import webfx.kit.mapper.peers.javafxgraphics.base.NodePeerMixin;
import webfx.kit.mapper.peers.javafxgraphics.gwt.shared.HtmlSvgNodePeer;
import webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlPaints;
import webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlTransforms;
import webfx.platform.shared.util.Strings;

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
        Element container = getContainer();
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
    protected String toClipPath(Node clip) {
        if (clip != null) {
            if (clip instanceof Circle) {
                Circle c = (Circle) clip;
                return "circle(" + toPx(c.getRadius()) + " at " + toPx(c.getCenterX()) + " " + toPx(c.getCenterY());
            } else if (clip instanceof Rectangle) {
                Rectangle r = (Rectangle) clip;
                // inset(top right bottom left round top-radius right-radius bottom-radius left-radius)
                double top = r.getY();
                double left = r.getX();
                double right = left + r.getWidth();
                double bottom = top + r.getHeight();
/*
                double leftRadius = r.getArcWidth() / 2, rightRadius = leftRadius;
                double topRadius = r.getArcHeight() / 2, bottomRadius = topRadius;
                return "inset(" + toPx(top) + " " + toPx(right) + " " + toPx(bottom) + " " + toPx(left) + " round " + topRadius + "px " + rightRadius + "px " + bottomRadius + "px " + leftRadius + "px)";
*/
                // Note: replaced toPx(top) by top + "px" etc... to preserve precision (required for Mandelbrot thumbnails zoom effect as scale is between 1.0 and 1.1)
                return "polygon(" + left + "px " + top + "px, " + right + "px " + top + "px, " + right + "px " + bottom + "px, " + left + "px " + bottom + "px)";
            }
        }
        return null;
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
        return toPixel(position) + "px";
    }

    static long toPixel(double position) {
        return Math.round(position);
    }

}
