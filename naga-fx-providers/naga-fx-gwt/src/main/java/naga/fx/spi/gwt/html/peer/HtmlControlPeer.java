package naga.fx.spi.gwt.html.peer;

import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.HTMLElement;
import emul.javafx.geometry.Insets;
import emul.javafx.scene.control.Control;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.spi.peer.base.ControlPeerBase;
import naga.fx.spi.peer.base.ControlPeerMixin;

/**
 * @author Bruno Salmon
 */
abstract class HtmlControlPeer
        <N extends Control, NB extends ControlPeerBase<N, NB, NM>, NM extends ControlPeerMixin<N, NB, NM>>

        extends HtmlRegionPeer<N, NB, NM>
        implements ControlPeerMixin<N, NB, NM> {

    HtmlControlPeer(NB base, HTMLElement element) {
        super(base, element);
    }

    protected void prepareDomForAdditionalSkinChildren() {
        HTMLElement spanContainer = createAbsolutePositionSpan();
        setContainer(spanContainer);
        HTMLElement childrenContainer = createAbsolutePositionSpan();
        HtmlUtil.setStyleAttribute(childrenContainer, "pointer-events", "none");
        setChildrenContainer(childrenContainer);
        HtmlUtil.setChildren(spanContainer, getElement(), childrenContainer);
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
