package webfx.kit.mapper.peers.javafxcontrols.gwt.html;

import elemental2.dom.HTMLElement;
import javafx.scene.control.ToggleButton;
import webfx.kit.mapper.peers.javafxcontrols.base.ToggleButtonPeerBase;
import webfx.kit.mapper.peers.javafxcontrols.base.ToggleButtonPeerMixin;
import webfx.kit.mapper.peers.javafxgraphics.gwt.html.layoutmeasurable.HtmlLayoutCache;
import webfx.kit.mapper.peers.javafxgraphics.gwt.html.layoutmeasurable.HtmlLayoutMeasurable;
import webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;

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
