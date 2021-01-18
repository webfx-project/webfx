package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import dev.webfx.kit.mapper.peers.javafxgraphics.NodePeer;
import elemental2.dom.CSSProperties;
import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.*;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Transform;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.NodePeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.NodePeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.shared.HtmlSvgNodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlPaints;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlTransforms;
import dev.webfx.platform.shared.util.Strings;
import javafx.scene.transform.Translate;

import java.util.ArrayList;
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
            // We fix here a difference in the behaviour between HTML & JavaFX regarding borders: in JavaFX borders are
            // purely graphical with no impact on the coordinates system, while HTML makes borders act like padding.
            // For example, if a box has a 5px border, in HTML local coordinates (0,0) will start after the border, so
            // at (5,5) from the left top box corner, while in JavaFX local coordinates (0,0) stay at the corner.
            Parent parent = getNode().getParent();
            if (parent != null) { // So checking if the parent has a border
                NodePeer parentPeer = parent.getNodePeer();
                if (parentPeer instanceof HtmlSvgNodePeer) {
                    Element parentContainer = ((HtmlSvgNodePeer) parentPeer).getVisibleContainer();
                    if (parentContainer instanceof HTMLElement) {
                        CSSStyleDeclaration style = ((HTMLElement) parentContainer).style;
                        // Measuring the left and top border width
                        double leftBorder = style.borderLeftWidth.isString() ? fromPx(style.borderLeftWidth.asString()) : style.borderLeftWidth.isDouble() ? style.borderLeftWidth.asDouble() : 0;
                        double topBorder  = style.borderTopWidth.isString()  ? fromPx(style.borderTopWidth.asString())  : style.borderTopWidth.isDouble() ? style.borderTopWidth.asDouble() : 0;
                        // If there is a border, we add a node translation to revert the HTML behaviour
                        if (leftBorder != 0 || topBorder != 0) {
                            // First making a copy because the original is an observable list bound for updates
                            localToParentTransforms = new ArrayList<>(localToParentTransforms);
                            // Adding the revert translation that will finally emulate the same behaviour as JavaFX
                            localToParentTransforms.add(0, new Translate(-leftBorder, -topBorder));
                        }
                    }
                }
            }
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

    public static double fromPx(String px) {
        return px == null || !px.endsWith("px") ? 0 : Double.parseDouble(px.substring(0, px.length() - 2));
    }

}
