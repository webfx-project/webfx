package dev.webfx.kit.mapper.peers.javafxcontrols.gwtj2cl.html;

import dev.webfx.kit.launcher.aria.AriaRole;
import elemental2.dom.HTMLElement;
import javafx.scene.control.Hyperlink;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.ButtonBasePeerBase;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.ButtonBasePeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html.layoutmeasurable.HtmlLayoutMeasurableNoGrow;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.util.HtmlUtil;

/**
 * @author Bruno Salmon
 */
public final class HtmlHyperlinkPeer
        <N extends Hyperlink, NB extends ButtonBasePeerBase<N, NB, NM>, NM extends ButtonBasePeerMixin<N, NB, NM>>

        extends HtmlButtonBasePeer<N, NB, NM>
        implements HtmlLayoutMeasurableNoGrow {

    public HtmlHyperlinkPeer() {
        this((NB) new ButtonBasePeerBase(), HtmlUtil.createElement("fx-hyperlink"));
    }

    public HtmlHyperlinkPeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    protected AriaRole getAriaRoleDefault() {
        return AriaRole.LINK;
    }
}
