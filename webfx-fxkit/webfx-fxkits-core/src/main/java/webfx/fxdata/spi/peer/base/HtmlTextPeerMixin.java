package webfx.fxdata.spi.peer.base;

import webfx.fx.spi.peer.base.RegionPeerMixin;
import webfx.fxdata.control.HtmlText;

/**
 * @author Bruno Salmon
 */
public interface HtmlTextPeerMixin
        <N extends HtmlText, NB extends HtmlTextPeerBase<N, NB, NM>, NM extends HtmlTextPeerMixin<N, NB, NM>>

        extends RegionPeerMixin<N, NB, NM> {

    void updateText(String text);

}
