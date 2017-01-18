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
        implements LabelPeerMixin<N, NB, NM>, HtmlLayoutMeasurable {

    public HtmlLabelPeer() {
        this((NB) new LabelPeerBase());
    }

    public HtmlLabelPeer(NB base) {
        super(base, HtmlUtil.createSpanElement());
        // to have the text vertically centered, we need to set the line-height
        setElementStyleAttribute("line-height", "100%"); // when expressed as %, it's regarding the font height (and not the node height)
    }

    @Override
    public void updateHeight(Double height) {
        super.updateHeight(height);
        // To keep the text vertically centered, we express the line-height in px
        if (height != 0)
            setElementStyleAttribute("line-height", toPx(height)); // Same height as the node
    }
}
