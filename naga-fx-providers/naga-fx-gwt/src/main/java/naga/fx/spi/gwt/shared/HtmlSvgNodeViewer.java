package naga.fx.spi.gwt.shared;

import elemental2.Element;
import elemental2.Event;
import naga.commons.util.Strings;
import naga.fx.scene.Node;
import naga.fx.scene.Scene;
import naga.fx.scene.SceneRequester;
import naga.fx.scene.effect.BlendMode;
import naga.fx.scene.effect.Effect;
import naga.fx.scene.input.MouseEvent;
import naga.fx.scene.text.Font;
import naga.fx.scene.text.FontPosture;
import naga.fx.scene.transform.Transform;
import naga.fx.spi.gwt.html.viewer.HtmlNodeViewer;
import naga.fx.spi.gwt.svg.view.SvgNodeViewer;
import naga.fx.spi.gwt.util.DomType;
import naga.fx.spi.gwt.util.HtmlTransforms;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.spi.gwt.util.SvgTransforms;
import naga.fx.spi.viewer.NodeViewer;
import naga.fx.spi.viewer.base.NodeViewerBase;
import naga.fx.spi.viewer.base.NodeViewerImpl;
import naga.fx.spi.viewer.base.NodeViewerMixin;

import java.util.Collection;
import java.util.Objects;

/**
 * @author Bruno Salmon
 */
public abstract class HtmlSvgNodeViewer
        <E extends Element, N extends Node, NB extends NodeViewerBase<N, NB, NM>, NM extends NodeViewerMixin<N, NB, NM>>
        extends NodeViewerImpl<N, NB, NM> {

    private final E element;
    private Element container;
    protected DomType containerType;

    public HtmlSvgNodeViewer(NB base, E element) {
        super(base);
        this.element = element;
        setContainer(element);
    }

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        super.bind(node, sceneRequester);
    }

    public E getElement() {
        return element;
    }

    public void setContainer(Element container) {
        this.container = container;
        containerType = container != element || this instanceof SvgNodeViewer ? DomType.SVG : DomType.HTML;
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
    public void updateLocalToParentTransforms(Collection<Transform> localToParentTransforms) {
        boolean isSvg = containerType == DomType.SVG;
        setElementAttribute("transform", isSvg ? SvgTransforms.toSvgTransforms(localToParentTransforms) : HtmlTransforms.toHtmlTransforms(localToParentTransforms));
    }

    private static MouseEvent toMouseEvent(Event e) {
        return new MouseEvent(null, 0, 0, 0, 0, null, 0, false, false, false, false, false, false, false, false, false, false, null);
    }

    protected void setElementTextContent(String textContent) {
        element.textContent = Strings.toSafeString(textContent); // Using a safe string to avoid "undefined" with IE
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
        NodeViewer nodeViewer = node.getOrCreateAndBindNodeViewer();
        if (nodeViewer instanceof SvgNodeViewer) // SvgNodeViewer case
            return ((SvgNodeViewer) nodeViewer).getElement();
        if (nodeViewer instanceof HtmlNodeViewer) // HtmlNodeViewer case
            return ((HtmlNodeViewer) nodeViewer).getContainer();
        return null; // Shouldn't happen...
    }
}
