package webfx.fxkit.extra.mapper.spi.peer.impl.extra;

import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.base.RegionPeerMixin;
import webfx.fxkit.extra.controls.html.HtmlText;

/**
 * @author Bruno Salmon
 */
public interface HtmlTextPeerMixin
        <N extends HtmlText, NB extends HtmlTextPeerBase<N, NB, NM>, NM extends HtmlTextPeerMixin<N, NB, NM>>

        extends RegionPeerMixin<N, NB, NM> {

    void updateText(String text);

}
