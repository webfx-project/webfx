package webfx.fxkits.extra.mapper.spi.peer.impl;

import webfx.fxkits.core.mapper.spi.impl.peer.RegionPeerMixin;
import webfx.fxkits.extra.control.HtmlText;

/**
 * @author Bruno Salmon
 */
public interface HtmlTextPeerMixin
        <N extends HtmlText, NB extends HtmlTextPeerBase<N, NB, NM>, NM extends HtmlTextPeerMixin<N, NB, NM>>

        extends RegionPeerMixin<N, NB, NM> {

    void updateText(String text);

}
