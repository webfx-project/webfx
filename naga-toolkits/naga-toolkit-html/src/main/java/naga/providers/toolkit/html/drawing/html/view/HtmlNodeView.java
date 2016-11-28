package naga.providers.toolkit.html.drawing.html.view;

import elemental2.HTMLElement;
import naga.providers.toolkit.html.events.HtmlMouseEvent;
import naga.providers.toolkit.html.util.HtmlTransforms;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.drawing.effect.BlendMode;
import naga.toolkit.drawing.effect.Effect;
import naga.toolkit.drawing.effect.GaussianBlur;
import naga.toolkit.drawing.geometry.VPos;
import naga.toolkit.drawing.scene.Node;
import naga.toolkit.drawing.shape.Circle;
import naga.toolkit.drawing.shape.Rectangle;
import naga.toolkit.drawing.spi.view.base.NodeViewBase;
import naga.toolkit.drawing.spi.view.base.NodeViewImpl;
import naga.toolkit.drawing.spi.view.base.NodeViewMixin;
import naga.toolkit.drawing.text.Font;
import naga.toolkit.drawing.text.FontPosture;
import naga.toolkit.drawing.text.TextAlignment;
import naga.toolkit.spi.events.MouseEvent;
import naga.toolkit.spi.events.UiEventHandler;
import naga.toolkit.transform.Transform;

import java.util.Collection;
import java.util.Objects;

/**
 * @author Bruno Salmon
 */
public abstract class HtmlNodeView
        <N extends Node, NV extends NodeViewBase<N, NV, NM>, NM extends NodeViewMixin<N, NV, NM>>
        extends NodeViewImpl<N, NV, NM> {

    private final HTMLElement element;

    HtmlNodeView(NV base, HTMLElement element) {
        super(base);
        this.element = element;
    }

    public HTMLElement getElement() {
        return element;
    }

    @Override
    public void updateMouseTransparent(Boolean mouseTransparent) {
        getElement().style.pointerEvents = mouseTransparent ? "none" : null;
    }

    @Override
    public void updateVisible(Boolean visible) {
        getElement().style.visibility = visible ? null : "hidden";
    }

    @Override
    public void updateOpacity(Double opacity) {
        getElement().style.opacity = opacity == 1d ? null : opacity;
    }

    @Override
    public void updateClip(Node clip) {
        setElementStyleAttribute("clip-path", toClipPath(clip));
    }

    private String toClipPath(Node clip) {
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
    public void updateBlendMode(BlendMode blendMode) {
        String svgBlend = toSvgBlendMode(blendMode);
        setElementStyleAttribute("mix-blend-mode", svgBlend);
    }

    @Override
    public void updateEffect(Effect effect) {
        setElementStyleAttribute("filter", toCssFilter(effect));
    }

    private static String toCssFilter(Effect effect) {
        if (effect == null)
            return null;
        if (effect instanceof GaussianBlur) {
            return "blur(" + ((GaussianBlur) effect).getSigma() + "px)";
        }
        return null;
    }

    @Override
    public void updateLocalToParentTransforms(Collection<Transform> localToParentTransforms) {
        getElement().style.transform = HtmlTransforms.toHtmlTransforms(localToParentTransforms);
    }

    @Override
    public void updateOnMouseClicked(UiEventHandler<? super MouseEvent> onMouseClicked) {
        element.onclick = onMouseClicked == null ? null : e -> {
            onMouseClicked.handle(new HtmlMouseEvent(e));
            return null;
        };
    }

    void setElementAttribute(String name, String value) {
        setElementAttribute(name, value, null);
    }

    void setElementAttribute(String name, String value, String skipValue) {
        if (Objects.equals(value, skipValue))
            element.removeAttribute(name);
        else
            element.setAttribute(name, value);
    }

    void setElementAttribute(String name, Double value) {
        setElementAttribute(name, value, null);
    }

    void setElementAttribute(String name, Double value, Double skipValue) {
        if (Objects.equals(value, skipValue))
            element.removeAttribute(name);
        else
            element.setAttribute(name, value);
    }

    void setElementAttribute(String name, Integer value) {
        setElementAttribute(name, value, null);
    }

    void setElementAttribute(String name, Integer value, Integer skipValue) {
        if (Objects.equals(value, skipValue))
            element.removeAttribute(name);
        else
            element.setAttribute(name, value);
    }

    void setElementStyleAttribute(String name, Object value) {
        HtmlUtil.setStyleAttribute(element, name, value);
    }

    void setHtmlFontAttributes(Font font) {
        if (font != null) {
            setElementStyleAttribute("font-family", font.getFamily());
            setElementStyleAttribute("font-style", font.getPosture() == FontPosture.ITALIC ? "italic" : "normal");
            setElementStyleAttribute("font-weight", font.getWeight() == null ? 0 : font.getWeight().getWeight());
            setElementStyleAttribute("font-size", toPx(font.getSize()));
        }
    }

    void setElementTextContent(String textContent) {
        element.textContent = textContent;
    }

    static String toCssVerticalAlignment(VPos vpos) {
        if (vpos != null)
            switch (vpos) {
                case TOP: return "top";
                case CENTER: return "middle";
                case BASELINE: return "baseline";
                case BOTTOM: return "bottom";
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

    static String toSvgBlendMode(BlendMode blendMode) {
        if (blendMode != null)
            switch (blendMode) {
                case SRC_OVER: return "";
                case SRC_ATOP: return "";
                case ADD: return "";
                case MULTIPLY: return "multiply";
                case SCREEN: return "screen";
                case OVERLAY: return "overlay";
                case DARKEN: return "darken";
                case LIGHTEN: return "lighten";
                case COLOR_DODGE: return "color-dodge";
                case COLOR_BURN: return "color-burn";
                case HARD_LIGHT: return "hard-light";
                case SOFT_LIGHT: return "soft-light";
                case DIFFERENCE: return "difference";
                case EXCLUSION: return "exclusion";
                case RED: return "";
                case GREEN: return "";
                case BLUE: return "";
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
