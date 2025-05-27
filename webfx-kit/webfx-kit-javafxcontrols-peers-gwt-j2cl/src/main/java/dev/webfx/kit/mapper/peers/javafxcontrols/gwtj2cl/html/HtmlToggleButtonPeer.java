package dev.webfx.kit.mapper.peers.javafxcontrols.gwtj2cl.html;

import dev.webfx.kit.launcher.aria.AriaRole;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.ToggleButtonPeerBase;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.ToggleButtonPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html.layoutmeasurable.HtmlLayoutCache;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html.layoutmeasurable.HtmlLayoutMeasurable;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.util.HtmlUtil;
import dev.webfx.platform.util.collection.Collections;
import elemental2.dom.HTMLElement;
import javafx.scene.control.ToggleButton;

/**
 * @author Bruno Salmon
 */
public final class HtmlToggleButtonPeer
        <N extends ToggleButton, NB extends ToggleButtonPeerBase<N, NB, NM>, NM extends ToggleButtonPeerMixin<N, NB, NM>>

        extends HtmlButtonBasePeer<N, NB, NM>
        implements ToggleButtonPeerMixin<N, NB, NM>, HtmlLayoutMeasurable {

    public HtmlToggleButtonPeer() {
        this((NB) new ToggleButtonPeerBase(), HtmlUtil.createElement("fx-togglebutton"));
    }

    public HtmlToggleButtonPeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    protected AriaRole getAriaRoleDefault() {
        return AriaRole.BUTTON;
    }

    @Override
    protected Boolean isAriaSelectedDefault() {
        return getNode().isSelected();
    }

    @Override
    public void updateSelected(Boolean selected) {
        Collections.addIfNotContainsOrRemove(getNode().getStyleClass(), selected,"selected");
        updateAriaSelectedAndTabindex(getNodeProperties());
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
