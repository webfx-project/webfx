package webfx.fxkit.gwt.mapper.html.peer.javafxcontrols;

import elemental2.dom.HTMLElement;
import javafx.scene.control.ToggleButton;
import webfx.fxkit.gwt.mapper.html.peer.HtmlLayoutCache;
import webfx.fxkit.gwt.mapper.html.peer.HtmlLayoutMeasurable;
import webfx.fxkit.gwt.mapper.util.HtmlUtil;
import webfx.fxkit.mapper.spi.impl.peer.javafxcontrols.ToggleButtonPeerBase;
import webfx.fxkit.mapper.spi.impl.peer.javafxcontrols.ToggleButtonPeerMixin;

/**
 * @author Bruno Salmon
 */
public final class HtmlToggleButtonPeer
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