package dev.webfx.kit.mapper.peers.javafxcontrols.elemental2.html;

import dev.webfx.kit.mapper.peers.javafxcontrols.base.ToggleButtonPeerBase;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.ToggleButtonPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.measurable.MeasurableCache;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.layoutmeasurable.HtmlMeasurable;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlUtil;
import dev.webfx.kit.util.aria.AriaRole;
import dev.webfx.platform.util.collection.Collections;
import elemental2.dom.HTMLElement;
import javafx.scene.control.ToggleButton;

/**
 * @author Bruno Salmon
 */
public final class HtmlToggleButtonPeer
        <N extends ToggleButton, NB extends ToggleButtonPeerBase<N, NB, NM>, NM extends ToggleButtonPeerMixin<N, NB, NM>>

        extends HtmlButtonBasePeer<N, NB, NM>
        implements ToggleButtonPeerMixin<N, NB, NM>, HtmlMeasurable {

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
        Collections.addIfNotContainsOrRemove(getNode().getStyleClass(), selected, "pseudo-selected");
        updateAriaSelectedAndTabindex(getNodeProperties());
    }

    @Override
    public double maxHeight(double width) {
        return prefHeight(width);
    }

    private final MeasurableCache cache = new MeasurableCache();
    @Override
    public MeasurableCache getCache() {
        return cache;
    }

}
