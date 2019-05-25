package webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.gwt.html;

import elemental2.dom.HTMLElement;
import javafx.scene.control.Hyperlink;
import webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.base.ButtonBasePeerBase;
import webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.base.ButtonBasePeerMixin;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.html.layoutmeasurable.HtmlLayoutMeasurableNoGrow;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.util.HtmlUtil;

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
