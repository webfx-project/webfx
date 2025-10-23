package dev.webfx.kit.mapper.peers.javafxcontrols.elemental2.html;

import dev.webfx.kit.util.aria.AriaRole;
import elemental2.dom.HTMLElement;
import javafx.scene.control.Hyperlink;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.ButtonBasePeerBase;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.ButtonBasePeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.layoutmeasurable.HtmlMeasurableNoGrow;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlUtil;

/**
 * @author Bruno Salmon
 */
public final class HtmlHyperlinkPeer
        <N extends Hyperlink, NB extends ButtonBasePeerBase<N, NB, NM>, NM extends ButtonBasePeerMixin<N, NB, NM>>

        extends HtmlButtonBasePeer<N, NB, NM>
        implements HtmlMeasurableNoGrow {

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
