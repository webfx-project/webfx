package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html;

import dev.webfx.kit.mapper.peers.javafxgraphics.base.NodePeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.NodePeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.shared.HtmlSvgNodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.shared.SvgRoot;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlPaints;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlTransforms;
import dev.webfx.kit.util.aria.AriaRole;
import dev.webfx.kit.util.aria.AriaSelectedAttribute;
import dev.webfx.platform.util.Booleans;
import dev.webfx.platform.util.Strings;
import elemental2.dom.CSSProperties;
import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import javafx.collections.ObservableMap;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Transform;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public abstract class HtmlNodePeer
        <N extends Node, NB extends NodePeerBase<N, NB, NM>, NM extends NodePeerMixin<N, NB, NM>>

        extends HtmlSvgNodePeer<HTMLElement, N, NB, NM> {

    public HtmlNodePeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    protected HtmlScenePeer getScenePeer() {
        return (HtmlScenePeer) super.getScenePeer();
    }

    @Override
    protected SvgRoot getSvgRoot() {
        return getScenePeer().getSvgRoot();
    }

    @Override
    protected void onNodePropertiesChanged(ObservableMap<Object, Object> nodeProperties) {
        updateAriaAttributes(nodeProperties);
    }

    private void updateAriaAttributes(ObservableMap<Object, Object> nodeProperties) {
        AriaRole ariaRole = getAriaRole(nodeProperties);
        setElementAttribute("role", Strings.toString(ariaRole));
        setElementAttribute("aria-label", Strings.toString(nodeProperties.get("aria-label")));
        setElementAttribute("aria-expanded", Strings.toString(nodeProperties.get("aria-expanded")));
        setElementAttribute("aria-readonly", Strings.toString(nodeProperties.get("aria-readonly")));
        setElementAttribute("aria-required", Strings.toString(nodeProperties.get("aria-required")));
        setElementAttribute("aria-invalid", Strings.toString(nodeProperties.get("aria-invalid")));
        updateAriaHidden(nodeProperties);
        updateAriaDisabled(nodeProperties);
        updateAriaSelectedAndTabindex(nodeProperties);
    }

    private AriaRole getAriaRole(ObservableMap<Object, Object> nodeProperties) {
        Object ariaRole = nodeProperties.get("aria-role");
        if (ariaRole instanceof AriaRole)
            return (AriaRole) ariaRole;
        return getAriaRoleDefault();
    }

    protected AriaRole getAriaRoleDefault() {
        return null;
    }

    protected Boolean isAriaSelectedDefault() {
        return null;
    }

    protected void updateAriaSelectedAndTabindex(ObservableMap<Object, Object> nodeProperties) {
        updateAriaSelectedAndTabindex(nodeProperties, getAriaRole(nodeProperties));
    }

    private void updateAriaSelectedAndTabindex(ObservableMap<Object, Object> nodeProperties, AriaRole ariaRole) {
        Object as = nodeProperties.get("aria-selected");
        Boolean ariaSelected = as instanceof Boolean ? (Boolean) as : isAriaSelectedDefault();
        Object asa = nodeProperties.get("aria-selected-attribute");
        AriaSelectedAttribute ariaSelectedAttribute;
        if (asa instanceof AriaSelectedAttribute)
            ariaSelectedAttribute = (AriaSelectedAttribute) asa;
        else
            ariaSelectedAttribute = getAriaSelectedAttributeDefault(nodeProperties);
        if (ariaSelectedAttribute != null)
            setElementAttribute(ariaSelectedAttribute.getAttributeName(), Strings.toString(ariaSelected));
        // The tabIndex is necessary only for focusable nodes
        if (getNode().isFocusTraversable()) {
            // If the node is not selectable (i.e., if ariaSelected == null), or if it is selectable but not selected,
            // we set tabIndex to -1, otherwise we set tabIndex to 0.
            setElementAttribute("tabindex", Booleans.isNotTrue(ariaSelected) ? "-1" : "0");
        }
    }

    protected AriaSelectedAttribute getAriaSelectedAttributeDefault(ObservableMap<Object, Object> nodeProperties) {
        AriaRole ariaRole = getAriaRole(nodeProperties);
        if (ariaRole != null)
            return ariaRole.getSelectedAttribute();
        return null;
    }

    protected void updateAriaHidden(ObservableMap<Object, Object> nodeProperties) {
        Object value = nodeProperties.get("aria-hidden");
        if (value == null)
            value = getNode().isVisible() ? null : "true";
        setElementAttribute("aria-hidden", Strings.toString(value));
    }

    protected void updateAriaDisabled(ObservableMap<Object, Object> nodeProperties) {
        Object value = nodeProperties.get("aria-disabled");
        if (value == null)
            value = getNode().isDisabled() ? "true" : null;
        setElementAttribute("aria-disabled", Strings.toString(value));
    }

    @Override
    public void updateVisible(Boolean visible) {
        super.updateVisible(visible);
        updateAriaHidden(getNodeProperties());
    }

    @Override
    public void updateDisabled(Boolean disabled) {
        super.updateDisabled(disabled);
        updateAriaDisabled(getNodeProperties());
    }

    @Override
    public void updateAllNodeTransforms(List<Transform> allNodeTransforms) {
        Element container = getVisibleContainer();
        if (!(container instanceof HTMLElement)) // for SVG elements
            super.updateAllNodeTransforms(allNodeTransforms);
        else { // for HTML elements
            String transform = HtmlTransforms.toHtmlTransforms(allNodeTransforms);
            CSSStyleDeclaration style = ((HTMLElement) container).style;
            style.transform = transform;
            // The transform origin depends on whether the transforms explicitly expresses the pivot. When using matrix,
            // the pivot is explicitly expressed and the origin is meant to be (0, 0). When using simpler transforms
            // such as rotate() or scale(), the pivot is implicit and is meant to be the center of the node.
            String cssTransformOrigin;
            if (Strings.contains(transform, "matrix"))
                cssTransformOrigin = "0px 0px";
            else if (getNode() instanceof Text) // special case tt make labels on Material Design aligned with left text input
                cssTransformOrigin = "center left"; // TODO: check if side-effect (move code to CSS in that case)
            else
                cssTransformOrigin = "center";
            style.transformOrigin = CSSProperties.TransformOriginUnionType.of(cssTransformOrigin);
        }
    }

    @Override
    public void updateEffect(Effect effect) {
        String boxShadow = toBoxShadow(effect);
        CSSStyleDeclaration style = getEffectElement().style;
        style.boxShadow = boxShadow;
        super.updateEffect(boxShadow != null ? null : effect); // other effects managed by filter function
    }

    @Override
    public void updateStyle(String style) {
        if (Strings.startsWith(style, "--")) {
            int p = style.indexOf(":");
            if (p > 0) {
                Element container = getVisibleContainer();
                CSSStyleDeclaration htmlStyle = ((HTMLElement) container).style;
                String propertyName = style.substring(0, p).trim();
                String propertyValue = Strings.removeSuffix(style.substring(p + 1).trim(), ";");
                htmlStyle.setProperty(propertyName, propertyValue);
            }
        }
    }

    protected HTMLElement getEffectElement() {
        return getElement();
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
            // Not supported by browser, so doing a gaussian blur instead
            return "blur(" + GaussianBlur.getSigma(((BoxBlur) effect).getWidth()) + "px)";
        if (effect instanceof DropShadow) {
            DropShadow dropShadow = (DropShadow) effect;
            return "drop-shadow(" + toPx(dropShadow.getOffsetX()) + " " + toPx(dropShadow.getOffsetY()) + " " + toPx(dropShadow.getRadius() / 2) + " " + HtmlPaints.toCssColor(dropShadow.getColor()) + ")";
        }
        return null;
    }

    public static String toCssTextAlignment(TextAlignment textAlignment) {
        if (textAlignment != null) {
            switch (textAlignment) {
                case LEFT: return "left";
                case CENTER: return "center";
                case RIGHT: return "right";
            }
        }
        return null;
    }

    public static String toCssTextAlignment(Pos pos) {
        return pos == null ? null : toCssTextAlignment(pos.getHpos());
    }

    static String toCssTextAlignment(HPos hPos) {
        if (hPos != null) {
            switch (hPos) {
                case LEFT: return "left";
                case CENTER: return "center";
                case RIGHT: return "right";
            }
        }
        return null;
    }

    public static String toPx(double position) {
        return position + "px";
    }

}
