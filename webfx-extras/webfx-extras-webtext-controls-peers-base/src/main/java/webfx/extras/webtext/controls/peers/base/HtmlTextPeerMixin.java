package webfx.extras.webtext.controls.peers.base;

import webfx.extras.webtext.controls.HtmlText;
import webfx.kit.mapper.peers.javafxcontrols.base.ControlPeerMixin;

/**
 * @author Bruno Salmon
 */
public interface HtmlTextPeerMixin
        <N extends HtmlText, NB extends HtmlTextPeerBase<N, NB, NM>, NM extends HtmlTextPeerMixin<N, NB, NM>>

        extends ControlPeerMixin<N, NB, NM> {

    void updateText(String text);

}
