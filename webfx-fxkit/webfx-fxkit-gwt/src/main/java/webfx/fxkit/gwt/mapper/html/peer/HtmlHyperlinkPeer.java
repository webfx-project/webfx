package webfx.fxkit.gwt.mapper.html.peer;

import elemental2.dom.HTMLElement;
import emul.javafx.scene.control.Hyperlink;
import webfx.fxkit.gwt.mapper.util.HtmlUtil;
import webfx.fxkits.core.mapper.spi.impl.peer.ButtonBasePeerBase;
import webfx.fxkits.core.mapper.spi.impl.peer.ButtonBasePeerMixin;

/**
 * @author Bruno Salmon
 */
public final class HtmlHyperlinkPeer
        <N extends Hyperlink, NB extends ButtonBasePeerBase<N, NB, NM>, NM extends ButtonBasePeerMixin<N, NB, NM>>

        extends HtmlButtonBasePeer<N, NB, NM>
        implements HtmlLayoutMeasurableNoGrow {

    public HtmlHyperlinkPeer() {
        this((NB) new ButtonBasePeerBase(), HtmlUtil.createElement("a"));
    }

    public HtmlHyperlinkPeer(NB base, HTMLElement element) {
        super(base, element);
        element.setAttribute("href", "#");
        element.onclick = e -> {
            e.preventDefault();
            return null;
        };
    }

}
