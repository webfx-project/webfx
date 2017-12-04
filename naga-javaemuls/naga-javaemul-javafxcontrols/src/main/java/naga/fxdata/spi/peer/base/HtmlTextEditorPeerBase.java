package naga.fxdata.spi.peer.base;

import naga.fxdata.control.HtmlTextEditor;

/**
 * @author Bruno Salmon
 */
public class HtmlTextEditorPeerBase
        <N extends HtmlTextEditor, NB extends HtmlTextEditorPeerBase<N, NB, NM>, NM extends HtmlTextEditorPeerMixin<N, NB, NM>>

        extends HtmlTextPeerBase<N, NB, NM> {

}
