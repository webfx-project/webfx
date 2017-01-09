package naga.fx.spi.gwt.html.peer;

import naga.fx.scene.control.Label;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.spi.peer.base.LabelPeerBase;
import naga.fx.spi.peer.base.LabelPeerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlLabelPeer
        <N extends Label, NB extends LabelPeerBase<N, NB, NM>, NM extends LabelPeerMixin<N, NB, NM>>
        extends HtmlLabeledPeer<N, NB, NM>
        implements LabelPeerMixin<N, NB, NM>, HtmlLayoutMeasurable {

    public HtmlLabelPeer() {
        this((NB) new LabelPeerBase());
    }

    public HtmlLabelPeer(NB base) {
        super(base, HtmlUtil.createSpanElement());
        setElementStyleAttribute("line-height", "100%");
    }
}
