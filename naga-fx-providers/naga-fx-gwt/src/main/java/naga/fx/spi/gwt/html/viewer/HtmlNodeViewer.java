package naga.fx.spi.gwt.html.viewer;

import elemental2.HTMLElement;
import naga.fx.spi.gwt.shared.HtmlSvgNodeViewer;
import naga.fx.spi.gwt.util.DomType;
import naga.fx.scene.effect.Effect;
import naga.fx.scene.effect.GaussianBlur;
import naga.fx.scene.Node;
import naga.fx.scene.shape.Circle;
import naga.fx.scene.shape.Rectangle;
import naga.fx.spi.viewer.base.NodeViewerBase;
import naga.fx.spi.viewer.base.NodeViewerMixin;
import naga.fx.scene.text.TextAlignment;

/**
 * @author Bruno Salmon
 */
public abstract class HtmlNodeViewer
        <N extends Node, NB extends NodeViewerBase<N, NB, NM>, NM extends NodeViewerMixin<N, NB, NM>>

        extends HtmlSvgNodeViewer<HTMLElement, N, NB, NM> {

    HtmlNodeViewer(NB base, HTMLElement element) {
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
