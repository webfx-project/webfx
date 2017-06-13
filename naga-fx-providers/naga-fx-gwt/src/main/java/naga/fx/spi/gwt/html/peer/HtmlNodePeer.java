package naga.fx.spi.gwt.html.peer;

import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.HTMLElement;
import emul.javafx.scene.Node;
import emul.javafx.scene.effect.Effect;
import emul.javafx.scene.effect.GaussianBlur;
import emul.javafx.scene.shape.Circle;
import emul.javafx.scene.shape.Rectangle;
import emul.javafx.scene.text.TextAlignment;
import emul.javafx.scene.transform.Transform;
import emul.javafx.scene.transform.Translate;
import naga.commons.util.collection.Collections;
import naga.fx.spi.gwt.shared.HtmlSvgNodePeer;
import naga.fx.spi.gwt.util.DomType;
import naga.fx.spi.gwt.util.HtmlTransforms;
import naga.fx.spi.peer.base.NodePeerBase;
import naga.fx.spi.peer.base.NodePeerMixin;

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
        int size = Collections.size(localToParentTransforms);
        if (size == 0)
            updateHtmlTransform(null, null, null);
        else {
            if (size == 1) {
                Transform transform = localToParentTransforms.get(0);
                if (transform instanceof Translate) {
                    Translate translate = (Translate) transform;
                    updateHtmlTransform(toPx(translate.getX()), toPx(translate.getY()), null);
                    return;
                }
            }
            updateHtmlTransform(null, null, HtmlTransforms.toHtmlTransforms(localToParentTransforms));
        }
    }

    private void updateHtmlTransform(String left, String top, String transform) {
        CSSStyleDeclaration style = getElement().style;
        style.left = left;
        style.top = top;
        style.transform = transform;
    }

    @Override
    protected boolean isStyleAttribute(String name) {
        if (containerType == DomType.HTML)
            switch (name) {
                case "pointer-events":
                case "visibility":
                case "opacity":
                case "clip-path":
                case "mix-blend-mode":
                case "filter":
                case "font-family":
                case "font-style":
                case "font-weight":
                case "font-size":
                case "transform":
                    return true;
            }
        return false;
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
                double leftRadius = r.getArcWidth() / 2, rightRadius = leftRadius;
                double topRadius = r.getArcHeight() / 2, bottomRadius = topRadius;
                return "inset(" + toPx(top) + " " + toPx(right) + " " + toPx(bottom) + " " + toPx(left) + " round " + topRadius + "px " + rightRadius + "px " + bottomRadius + "px" + leftRadius + "px)";
            }
        }
        return null;
    }

    @Override
    protected String toFilter(Effect effect) {
        if (effect == null)
            return null;
        if (effect instanceof GaussianBlur) {
            return "blur(" + ((GaussianBlur) effect).getSigma() + "px)";
        }
        return null;
    }

    static String toCssTextAlignment(TextAlignment textAlignment) {
        if (textAlignment != null)
            switch (textAlignment) {
                case LEFT: return "left";
                case CENTER: return "center";
                case RIGHT: return "right";
            }
        return null;
    }

    static String toPx(double position) {
        return toPixel(position) + "px";
    }

    static long toPixel(double position) {
        return Math.round(position);
    }

}
