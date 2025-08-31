package dev.webfx.kit.mapper.peers.javafxcontrols.gwtj2cl.html;

import dev.webfx.kit.mapper.peers.javafxcontrols.base.RadioButtonPeerBase;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.RadioButtonPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html.NoWrapWhiteSpacePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html.layoutmeasurable.HtmlLayoutMeasurableNoGrow;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.util.HtmlUtil;
import dev.webfx.kit.util.aria.AriaRole;
import dev.webfx.platform.util.collection.Collections;
import elemental2.dom.HTMLElement;
import javafx.scene.control.RadioButton;

/**
 * @author Bruno Salmon
 */
public final class HtmlRadioButtonPeer
        <N extends RadioButton, NB extends RadioButtonPeerBase<N, NB, NM>, NM extends RadioButtonPeerMixin<N, NB, NM>>

        extends HtmlButtonBasePeer<N, NB, NM>
        implements RadioButtonPeerMixin<N, NB, NM>, HtmlLayoutMeasurableNoGrow, NoWrapWhiteSpacePeer {

    public HtmlRadioButtonPeer() {
        this((NB) new RadioButtonPeerBase(), HtmlUtil.createElement("fx-radiobutton"));
    }

    public HtmlRadioButtonPeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    protected AriaRole getAriaRoleDefault() {
        return AriaRole.RADIO;
    }

    @Override
    protected Boolean isAriaSelectedDefault() {
        return getNode().isSelected();
    }

    @Override
    public void updateSelected(Boolean selected) {
        Collections.addIfNotContainsOrRemove(getNode().getStyleClass(), selected, "pseudo-selected");
        updateAriaSelectedAndTabindex(getNodeProperties());
    }

}
