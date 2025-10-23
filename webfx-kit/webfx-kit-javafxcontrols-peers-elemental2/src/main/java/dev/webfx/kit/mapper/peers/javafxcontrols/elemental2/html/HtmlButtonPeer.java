package dev.webfx.kit.mapper.peers.javafxcontrols.elemental2.html;

import dev.webfx.kit.util.aria.AriaRole;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.ButtonPeerBase;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.ButtonPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.NoWrapWhiteSpacePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlUtil;
import elemental2.dom.HTMLElement;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;

/**
 * @author Bruno Salmon
 */
public final class HtmlButtonPeer
        <N extends Button, NB extends ButtonPeerBase<N, NB, NM>, NM extends ButtonPeerMixin<N, NB, NM>>

        extends HtmlButtonBasePeer<N, NB, NM>
        implements ButtonPeerMixin<N, NB, NM>, NoWrapWhiteSpacePeer {

    public HtmlButtonPeer() {
        this((NB) new ButtonPeerBase(), HtmlUtil.createElement("fx-button"));
    }

    public HtmlButtonPeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    protected AriaRole getAriaRoleDefault() {
        return AriaRole.BUTTON;
    }

    @Override
    public void updateGraphic(Node graphic) {
        super.updateGraphic(graphic);
        // Restoring pointer events (were disabled by prepareDomForAdditionalSkinChildren()) in case the graphic is clickable (ex: radio button)
        if (graphic instanceof ButtonBase)
            HtmlUtil.setStyleAttribute(getChildrenContainer(), "pointer-events", "auto");

    }
}
