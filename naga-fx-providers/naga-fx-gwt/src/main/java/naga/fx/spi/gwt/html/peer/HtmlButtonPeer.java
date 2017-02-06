package naga.fx.spi.gwt.html.peer;

import elemental2.HTMLElement;
import naga.fx.spi.gwt.util.HtmlUtil;
import emul.javafx.scene.control.Button;
import naga.fx.spi.peer.base.ButtonPeerBase;
import naga.fx.spi.peer.base.ButtonPeerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlButtonPeer
        <N extends Button, NB extends ButtonPeerBase<N, NB, NM>, NM extends ButtonPeerMixin<N, NB, NM>>

        extends HtmlButtonBasePeer<N, NB, NM>
        implements ButtonPeerMixin<N, NB, NM>, HtmlLayoutMeasurable {

    public HtmlButtonPeer() {
        this((NB) new ButtonPeerBase(), HtmlUtil.createButtonElement());
    }

    public HtmlButtonPeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    public double maxHeight(double width) {
        return prefHeight(width);
    }

    private final HtmlLayoutCache cache = new HtmlLayoutCache();
    @Override
    public HtmlLayoutCache getCache() {
        return cache;
    }

}
