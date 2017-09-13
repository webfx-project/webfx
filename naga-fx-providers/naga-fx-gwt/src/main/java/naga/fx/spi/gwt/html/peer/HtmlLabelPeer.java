package naga.fx.spi.gwt.html.peer;

import emul.javafx.scene.control.Label;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.spi.peer.base.LabelPeerBase;
import naga.fx.spi.peer.base.LabelPeerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlLabelPeer
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
        subtractNodePaddingBorderWhenUpdatingSize = true;
    }
}
