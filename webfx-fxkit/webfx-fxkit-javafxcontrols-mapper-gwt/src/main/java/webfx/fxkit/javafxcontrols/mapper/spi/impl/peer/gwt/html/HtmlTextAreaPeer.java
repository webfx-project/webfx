package webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.gwt.html;

import elemental2.dom.HTMLElement;
import javafx.scene.control.TextArea;
import webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.base.TextAreaPeerBase;
import webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.base.TextAreaPeerMixin;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.html.layoutmeasurable.HtmlLayoutMeasurable;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.util.HtmlUtil;

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
