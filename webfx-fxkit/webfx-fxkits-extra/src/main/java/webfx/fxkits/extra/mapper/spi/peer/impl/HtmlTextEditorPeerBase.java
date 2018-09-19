package webfx.fxkits.extra.mapper.spi.peer.impl;

import webfx.fxkits.extra.control.HtmlTextEditor;

/**
 * @author Bruno Salmon
 */
public class HtmlTextEditorPeerBase
        <N extends HtmlTextEditor, NB extends HtmlTextEditorPeerBase<N, NB, NM>, NM extends HtmlTextEditorPeerMixin<N, NB, NM>>

        extends HtmlTextPeerBase<N, NB, NM> {

}
