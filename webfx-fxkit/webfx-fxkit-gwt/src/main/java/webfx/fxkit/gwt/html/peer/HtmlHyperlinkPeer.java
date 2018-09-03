package webfx.fxkit.gwt.html.peer;

import elemental2.dom.HTMLElement;
import emul.javafx.scene.control.Hyperlink;
import webfx.fxkit.gwt.util.HtmlUtil;
import webfx.fxkits.core.spi.peer.base.ButtonBasePeerBase;
import webfx.fxkits.core.spi.peer.base.ButtonBasePeerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlHyperlinkPeer
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
