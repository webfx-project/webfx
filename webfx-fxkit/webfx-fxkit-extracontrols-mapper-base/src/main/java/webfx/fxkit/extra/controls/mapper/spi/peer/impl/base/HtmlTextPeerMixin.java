package webfx.fxkit.extra.controls.mapper.spi.peer.impl.base;

import webfx.fxkit.extra.controls.html.HtmlText;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.base.RegionPeerMixin;

/**
 * @author Bruno Salmon
 */
public interface HtmlTextPeerMixin
        <N extends HtmlText, NB extends HtmlTextPeerBase<N, NB, NM>, NM extends HtmlTextPeerMixin<N, NB, NM>>

        extends RegionPeerMixin<N, NB, NM> {

    void updateText(String text);

}
