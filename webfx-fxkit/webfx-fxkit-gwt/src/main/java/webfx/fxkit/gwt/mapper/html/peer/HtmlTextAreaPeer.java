package webfx.fxkit.gwt.mapper.html.peer;

import elemental2.dom.HTMLElement;
import emul.javafx.scene.control.TextArea;
import webfx.fxkit.gwt.mapper.util.HtmlUtil;
import webfx.fxkits.core.mapper.spi.impl.peer.TextAreaPeerBase;
import webfx.fxkits.core.mapper.spi.impl.peer.TextAreaPeerMixin;

/**
 * @author Bruno Salmon
 */
public final class HtmlTextAreaPeer
        <N extends TextArea, NB extends TextAreaPeerBase<N, NB, NM>, NM extends TextAreaPeerMixin<N, NB, NM>>

        extends HtmlTextInputControlPeer<N, NB, NM>
        implements TextAreaPeerMixin<N, NB, NM>, HtmlLayoutMeasurable {

    public HtmlTextAreaPeer() {
        this((NB) new TextAreaPeerBase(), HtmlUtil.createTextArea());
    }

    public HtmlTextAreaPeer(NB base, HTMLElement element) {
        super(base, element);
        element.style.resize = "none"; // To disable the html text area resize feature
    }

}
