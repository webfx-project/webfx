package naga.providers.toolkit.html.drawing.html.view;

import elemental2.HTMLElement;
import naga.providers.toolkit.html.drawing.shared.HtmlSvgNodeView;
import naga.providers.toolkit.html.util.DomType;
import naga.toolkit.drawing.effect.Effect;
import naga.toolkit.drawing.effect.GaussianBlur;
import naga.toolkit.drawing.scene.Node;
import naga.toolkit.drawing.shape.Circle;
import naga.toolkit.drawing.shape.Rectangle;
import naga.toolkit.drawing.spi.view.base.NodeViewBase;
import naga.toolkit.drawing.spi.view.base.NodeViewMixin;
import naga.toolkit.drawing.text.TextAlignment;

/**
 * @author Bruno Salmon
 */
public abstract class HtmlNodeView
        <N extends Node, NV extends NodeViewBase<N, NV, NM>, NM extends NodeViewMixin<N, NV, NM>>
        extends HtmlSvgNodeView<HTMLElement, N, NV, NM> {

    HtmlNodeView(NV base, HTMLElement element) {
        super(base, element);
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
