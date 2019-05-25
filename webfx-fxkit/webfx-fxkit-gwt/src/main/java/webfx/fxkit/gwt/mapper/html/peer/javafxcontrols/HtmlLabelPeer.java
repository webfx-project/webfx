package webfx.fxkit.gwt.mapper.html.peer.javafxcontrols;

import javafx.scene.control.Label;
import webfx.fxkit.gwt.mapper.html.peer.HtmlLayoutMeasurableNoGrow;
import webfx.fxkit.gwt.mapper.util.HtmlUtil;
import webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.base.LabelPeerBase;
import webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.base.LabelPeerMixin;

/**
 * @author Bruno Salmon
 */
public final class HtmlLabelPeer
        <N extends Label, NB extends LabelPeerBase<N, NB, NM>, NM extends LabelPeerMixin<N, NB, NM>>
        extends HtmlLabeledPeer<N, NB, NM>
        implements LabelPeerMixin<N, NB, NM>, HtmlLayoutMeasurableNoGrow {

    public HtmlLabelPeer() {
        this((NB) new LabelPeerBase());
    }

    public HtmlLabelPeer(NB base) {
        super(base, HtmlUtil.createSpanElement());
        // to have the text vertically centered, we need to set the line-height
        setElementStyleAttribute("line-height", "100%"); // when expressed as %, it's regarding the font height (and not the node height)
        subtractCssPaddingBorderWhenUpdatingSize = true;
    }
}
