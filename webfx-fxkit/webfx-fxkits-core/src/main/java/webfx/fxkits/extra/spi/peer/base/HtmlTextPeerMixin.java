package webfx.fxkits.extra.spi.peer.base;

import webfx.fxkits.core.spi.peer.base.RegionPeerMixin;
import webfx.fxkits.extra.control.HtmlText;

/**
 * @author Bruno Salmon
 */
public interface HtmlTextPeerMixin
        <N extends HtmlText, NB extends HtmlTextPeerBase<N, NB, NM>, NM extends HtmlTextPeerMixin<N, NB, NM>>

        extends RegionPeerMixin<N, NB, NM> {

    void updateText(String text);

}
