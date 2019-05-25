package webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.gwt.html;

import javafx.scene.control.Label;
import webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.base.LabelPeerBase;
import webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.base.LabelPeerMixin;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.html.layoutmeasurable.HtmlLayoutMeasurableNoGrow;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.util.HtmlUtil;

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
