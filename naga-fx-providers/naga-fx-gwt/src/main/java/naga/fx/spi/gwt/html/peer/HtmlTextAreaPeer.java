package naga.fx.spi.gwt.html.peer;

import elemental2.HTMLElement;
import emul.javafx.scene.control.TextArea;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.spi.peer.base.TextAreaPeerBase;
import naga.fx.spi.peer.base.TextAreaPeerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlTextAreaPeer
        <N extends TextArea, NB extends TextAreaPeerBase<N, NB, NM>, NM extends TextAreaPeerMixin<N, NB, NM>>

        extends HtmlTextInputControlPeer<N, NB, NM>
        implements TextAreaPeerMixin<N, NB, NM>, HtmlLayoutMeasurable {

    public HtmlTextAreaPeer() {
        this((NB) new TextAreaPeerBase(), HtmlUtil.createTextArea());
    }

    public HtmlTextAreaPeer(NB base, HTMLElement element) {
        super(base, element);
    }

}
