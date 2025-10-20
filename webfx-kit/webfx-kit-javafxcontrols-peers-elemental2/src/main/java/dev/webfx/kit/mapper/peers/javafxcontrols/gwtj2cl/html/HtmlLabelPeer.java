package dev.webfx.kit.mapper.peers.javafxcontrols.gwtj2cl.html;

import javafx.scene.control.Label;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.LabelPeerBase;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.LabelPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.layoutmeasurable.HtmlMeasurableNoGrow;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlUtil;

/**
 * @author Bruno Salmon
 */
public final class HtmlLabelPeer
        <N extends Label, NB extends LabelPeerBase<N, NB, NM>, NM extends LabelPeerMixin<N, NB, NM>>
        extends HtmlLabeledPeer<N, NB, NM>
        implements LabelPeerMixin<N, NB, NM>, HtmlMeasurableNoGrow {

    public HtmlLabelPeer() {
        this((NB) new LabelPeerBase());
    }

    public HtmlLabelPeer(NB base) {
        super(base, HtmlUtil.createElement("fx-label"));
        // to have the text vertically centered, we need to set line-height: 100%
        // Commented because we want space between lines when the text is wrapped. TODO: check if there is an effect on text vertically centered
        // setElementStyleAttribute("line-height", "100%"); // 100% means node height = font height with no extra on top & bottom
    }
}
