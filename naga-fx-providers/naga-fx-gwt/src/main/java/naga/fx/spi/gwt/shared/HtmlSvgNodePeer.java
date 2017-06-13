package naga.fx.spi.gwt.shared;

import elemental2.Element;
import emul.javafx.scene.LayoutMeasurable;
import emul.javafx.scene.Node;
import emul.javafx.scene.Scene;
import emul.javafx.scene.effect.BlendMode;
import emul.javafx.scene.effect.Effect;
import emul.javafx.scene.input.MouseEvent;
import emul.javafx.scene.text.Font;
import emul.javafx.scene.text.FontPosture;
import emul.javafx.scene.transform.Transform;
import naga.commons.util.Strings;
import naga.fx.scene.SceneRequester;
import naga.fx.spi.gwt.html.peer.HtmlNodePeer;
import naga.fx.spi.gwt.svg.peer.SvgNodePeer;
import naga.fx.spi.gwt.util.DomType;
import naga.fx.spi.gwt.util.HtmlTransforms;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.spi.gwt.util.SvgTransforms;
import naga.fx.spi.peer.NodePeer;
import naga.fx.spi.peer.base.NodePeerBase;
import naga.fx.spi.peer.base.NodePeerImpl;
import naga.fx.spi.peer.base.NodePeerMixin;

import java.util.List;
import java.util.Objects;

/**
 * @author Bruno Salmon
 */
public abstract class HtmlSvgNodePeer
        <E extends Element, N extends Node, NB extends NodePeerBase<N, NB, NM>, NM extends NodePeerMixin<N, NB, NM>>
        extends NodePeerImpl<N, NB, NM> {

    private final E element;
    private Element container;
    protected DomType containerType;

    public HtmlSvgNodePeer(NB base, E element) {
        super(base);
        this.element = element;
        setContainer(element);
    }

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        super.bind(node, sceneRequester);
        getElement().onclick = e -> {
            node.fireEvent(toMouseEvent((elemental2.MouseEvent) e));
            return false; // To stop href navigation for example
        };
        element.onfocus = e -> {
            node.setFocused(true);
            return null;
        };
        element.onblur = e -> {
            node.setFocused(false);
            return null;
        };
    }

    public E getElement() {
        return element;
    }

    public void setContainer(Element container) {
        this.container = container;
        containerType = container != element || this instanceof SvgNodePeer ? DomType.SVG : DomType.HTML;
    }

    public Element getContainer() {
        return container;
    }

    protected boolean isStyleAttribute(String name) {
        return false;
    }

    protected void setElementStyleAttribute(String name, Object value) {
        HtmlUtil.setStyleAttribute(container, name, value);
    }

    @Override
    public void updateMouseTransparent(Boolean mouseTransparent) {
        setElementAttribute("pointer-events", mouseTransparent ? "none" : null);
    }

    @Override
    public void updateVisible(Boolean visible) {
        setElementAttribute("visibility", visible ? null : "hidden");
    }

    @Override
    public void updateOpacity(Double opacity) {
        setElementAttribute("opacity", opacity == 1d ? null : opacity);
    }

    @Override
    public void updateDisabled(Boolean disabled) {
        setElementAttribute("disabled", disabled ? "disabled" : null);
    }

    @Override
    public void updateClip(Node clip) {
        setElementAttribute("clip-path", toClipPath(clip));
    }

    protected abstract String toClipPath(Node clip);

    @Override
    public void updateBlendMode(BlendMode blendMode) {
        setElementStyleAttribute("mix-blend-mode", toSvgBlendMode(blendMode));
    }

    @Override
    public void updateEffect(Effect effect) {
        setElementAttribute("filter", effect == null ? null : toFilter(effect));
    }

    protected abstract String toFilter(Effect effect);

    @Override
    public void updateLocalToParentTransforms(List<Transform> localToParentTransforms) {
        boolean isSvg = containerType == DomType.SVG;
        setElementAttribute("transform", isSvg ? SvgTransforms.toSvgTransforms(localToParentTransforms) : HtmlTransforms.toHtmlTransforms(localToParentTransforms));
    }

    private static MouseEvent toMouseEvent(elemental2.MouseEvent me) {
        return new MouseEvent(MouseEvent.MOUSE_CLICKED, me.x, me.y, me.screenX, me.screenY, null, 1, me.shiftKey, me.ctrlKey, me.altKey, me.metaKey, false, false, false, false, false, false, null);
    }

    protected void setElementTextContent(String textContent) {
        String text = Strings.toSafeString(textContent);
        if (!Objects.equals(element.textContent, text)) {
            element.textContent = text; // Using a safe string to avoid "undefined" with IE
            clearLayoutCache();
        }
    }

    protected void clearLayoutCache() {
        if (this instanceof LayoutMeasurable)
            ((LayoutMeasurable) this).clearCache();
    }

    /* String attributes */

    protected void setElementAttribute(String name, String value, String skipValue) {
        if (skipValue != null && Objects.equals(value, skipValue))
            value = null;
        setElementAttribute(name, value);
    }

    protected void setElementAttribute(String name, String value) {
        if (isStyleAttribute(name))
            setElementStyleAttribute(name, value);
        else
            setElementAttribute(container, name, value);
    }

    private void setElementAttribute(Element e, String name, String value) {
        if (value == null)
            e.removeAttribute(name);
        else
            e.setAttribute(name, value);
    }

    /* Double attributes */

    protected void setElementAttribute(String name, Double value, Double skipValue) {
        if (skipValue != null && Objects.equals(value, skipValue))
            value = null;
        setElementAttribute(name, value);
    }

    protected void setElementAttribute(String name, Double value) {
        if (container == element && isStyleAttribute(name))
            setElementStyleAttribute(name, value);
        else
            setElementAttribute(container, name, value);
    }

    private void setElementAttribute(Element e, String name, Double value) {
        if (value == null)
            e.removeAttribute(name);
        else
            e.setAttribute(name, value);
    }

    protected void setFontAttributes(Font font) {
        if (font != null) {
            setElementAttribute("font-family", font.getFamily());
            setElementAttribute("font-style", font.getPosture() == FontPosture.ITALIC ? "italic" : "normal");
            setElementAttribute("font-weight", font.getWeight() == null ? 0d : font.getWeight().getWeight());
            setElementAttribute("font-size", toPx(font.getSize()));
        }
    }

    private static String toSvgBlendMode(BlendMode blendMode) {
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

    public static Element toElement(Node node, Scene scene) {
        node.setScene(scene);
        NodePeer nodePeer = node.getOrCreateAndBindNodePeer();
        if (nodePeer instanceof SvgNodePeer) // SvgNodePeer case
            return ((SvgNodePeer) nodePeer).getElement();
        if (nodePeer instanceof HtmlNodePeer) // HtmlNodePeer case
            return ((HtmlNodePeer) nodePeer).getContainer();
        return null; // Shouldn't happen...
    }
}
