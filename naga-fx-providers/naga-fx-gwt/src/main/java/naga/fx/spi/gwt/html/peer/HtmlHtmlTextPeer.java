package naga.fx.spi.gwt.html.peer;

import naga.commons.util.Strings;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fxdata.control.HtmlText;
import naga.fxdata.spi.peer.base.HtmlTextPeerBase;
import naga.fxdata.spi.peer.base.HtmlTextPeerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlHtmlTextPeer
        <N extends HtmlText, NB extends HtmlTextPeerBase<N, NB, NM>, NM extends HtmlTextPeerMixin<N, NB, NM>>
        extends HtmlRegionPeer<N, NB, NM>
        implements HtmlTextPeerMixin<N, NB, NM>, HtmlLayoutMeasurable {

    public HtmlHtmlTextPeer() {
        this((NB) new HtmlTextPeerBase());
    }

    HtmlHtmlTextPeer(NB base) {
        super(base, HtmlUtil.createSpanElement());
    }

    @Override
    public void updateText(String text) {
        getElement().innerHTML = Strings.toSafeString(text);
    }

    @Override
    public double minWidth(double height) {
        return 0;
    }
}