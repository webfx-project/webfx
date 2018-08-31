package webfx.fxdata.spi.peer.base;

import webfx.fxdata.control.HtmlTextEditor;

/**
 * @author Bruno Salmon
 */
public interface HtmlTextEditorPeerMixin
        <N extends HtmlTextEditor, NB extends HtmlTextEditorPeerBase<N, NB, NM>, NM extends HtmlTextEditorPeerMixin<N, NB, NM>>

        extends HtmlTextPeerMixin<N, NB, NM> {

}
