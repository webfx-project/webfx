package dev.webfx.kit.mapper.peers.javafxcontrols.gwt.html;

import elemental2.dom.HTMLElement;
import javafx.scene.control.CheckBox;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.CheckBoxPeerBase;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.CheckBoxPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;

/**
 * @author Bruno Salmon
 */
public final class HtmlCheckBoxPeer
        <N extends CheckBox, NB extends CheckBoxPeerBase<N, NB, NM>, NM extends CheckBoxPeerMixin<N, NB, NM>>

        extends HtmlButtonBasePeer<N, NB, NM>
        implements CheckBoxPeerMixin<N, NB, NM> {

    public HtmlCheckBoxPeer() {
        this((NB) new CheckBoxPeerBase(), HtmlUtil.createSpanElement());
    }

    public HtmlCheckBoxPeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    public void updateSelected(Boolean selected) {
        // Nothing to do as everything is managed by the skin
    }
}
