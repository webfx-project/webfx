package dev.webfx.kit.mapper.peers.javafxcontrols.gwt.html;

import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.HTMLElement;
import javafx.geometry.Insets;
import javafx.scene.control.Control;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.HtmlRegionPeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.ControlPeerBase;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.ControlPeerMixin;

/**
 * @author Bruno Salmon
 */
public abstract class HtmlControlPeer
        <N extends Control, NB extends ControlPeerBase<N, NB, NM>, NM extends ControlPeerMixin<N, NB, NM>>

        extends HtmlRegionPeer<N, NB, NM>
        implements ControlPeerMixin<N, NB, NM> {

    public HtmlControlPeer(NB base, HTMLElement element) {
        super(base, element);
    }

    protected void prepareDomForAdditionalSkinChildren() {
        // We need to set the cursor style to inherit, otherwise a call to setCursor() from the JavaFX application code
        // (which will be mapped to the skin container) won't be correctly displayed when hovering the area of that html
        // control element (ex: setting a hand cursor won't work on the button area which will still display the default
        // arrow cursor). Setting the cursor style to inherit fixes the issue (ex: the hand cursor set on the skin
        // container will also be displayed when hovering the button element).
        setElementStyleAttribute("cursor", "inherit");
        HTMLElement skinContainer = createAbsolutePositionSpan();
        setContainer(skinContainer);
        HTMLElement skinChildrenContainer = createAbsolutePositionSpan();
        HtmlUtil.setStyleAttribute(skinChildrenContainer, "pointer-events", "none");
        setChildrenContainer(skinChildrenContainer);
        HtmlUtil.setChildren(skinContainer, getElement(), skinChildrenContainer);
    }

    private static HTMLElement createAbsolutePositionSpan() {
        HTMLElement spanElement = HtmlUtil.absolutePosition(HtmlUtil.createSpanElement());
        CSSStyleDeclaration style = spanElement.style;
        // Positioned to left top corner by default
        style.left = "0px";
        style.top = "0px";
        return spanElement;
    }

    @Override
    public void updatePadding(Insets padding) {
        if (doesSkinRelyOnPeerToProvideVisualContent())
            super.updatePadding(padding);
    }

    protected boolean doesSkinRelyOnPeerToProvideVisualContent() {
        return getNode().shouldUseLayoutMeasurable();
    }
}
