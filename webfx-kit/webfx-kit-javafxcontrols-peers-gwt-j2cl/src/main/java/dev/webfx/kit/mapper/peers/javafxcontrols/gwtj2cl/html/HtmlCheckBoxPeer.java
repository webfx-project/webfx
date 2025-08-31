package dev.webfx.kit.mapper.peers.javafxcontrols.gwtj2cl.html;

import dev.webfx.kit.util.aria.AriaRole;
import dev.webfx.platform.util.collection.Collections;
import elemental2.dom.HTMLElement;
import javafx.scene.control.CheckBox;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.CheckBoxPeerBase;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.CheckBoxPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.util.HtmlUtil;

/**
 * @author Bruno Salmon
 */
public final class HtmlCheckBoxPeer
        <N extends CheckBox, NB extends CheckBoxPeerBase<N, NB, NM>, NM extends CheckBoxPeerMixin<N, NB, NM>>

        extends HtmlButtonBasePeer<N, NB, NM>
        implements CheckBoxPeerMixin<N, NB, NM> {

    public HtmlCheckBoxPeer() {
        this((NB) new CheckBoxPeerBase(), HtmlUtil.createElement("fx-checkbox"));
    }

    public HtmlCheckBoxPeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    protected AriaRole getAriaRoleDefault() {
        return AriaRole.CHECKBOX;
    }

    @Override
    protected Boolean isAriaSelectedDefault() {
        return getNode().isSelected();
    }

    @Override
    public void updateSelected(Boolean selected) {
        Collections.addIfNotContainsOrRemove(getNode().getStyleClass(), selected, "pseudo-selected");
        // Nothing to do graphically as the skin manages this, but we update the arial state
        updateAriaSelectedAndTabindex(getNodeProperties());
    }
}
