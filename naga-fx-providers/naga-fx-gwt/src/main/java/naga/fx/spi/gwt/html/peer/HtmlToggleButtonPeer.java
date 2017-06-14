package naga.fx.spi.gwt.html.peer;

import elemental2.dom.Event;
import elemental2.dom.HTMLElement;
import emul.javafx.scene.control.ToggleButton;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.spi.peer.base.ToggleButtonPeerBase;
import naga.fx.spi.peer.base.ToggleButtonPeerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlToggleButtonPeer
        <N extends ToggleButton, NB extends ToggleButtonPeerBase<N, NB, NM>, NM extends ToggleButtonPeerMixin<N, NB, NM>>

        extends HtmlButtonBasePeer<N, NB, NM>
        implements ToggleButtonPeerMixin<N, NB, NM>, HtmlLayoutMeasurable {

    public HtmlToggleButtonPeer() {
        this((NB) new ToggleButtonPeerBase(), HtmlUtil.createButtonElement());
    }

    public HtmlToggleButtonPeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    protected void onClickElement(Event e) {
        getNode().fire();
    }

    @Override
    public void updateSelected(Boolean selected) {
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
