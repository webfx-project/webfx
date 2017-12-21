package naga.fx.spi.gwt.html.peer;

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
        HTMLElement spanContainer = HtmlUtil.absolutePosition(HtmlUtil.createSpanElement());
        setContainer(spanContainer);
        HTMLElement childrenContainer = HtmlUtil.createSpanElement();
        HtmlUtil.setStyleAttribute(childrenContainer, "pointer-events", "none");
        setChildrenContainer(childrenContainer);
        HtmlUtil.setChildren(spanContainer, getElement(), childrenContainer);
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
