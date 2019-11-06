package webfx.kit.mapper.peers.javafxcontrols.gwt.html;

import elemental2.dom.HTMLElement;
import javafx.scene.control.TextArea;
import webfx.kit.mapper.peers.javafxcontrols.base.TextAreaPeerBase;
import webfx.kit.mapper.peers.javafxcontrols.base.TextAreaPeerMixin;
import webfx.kit.mapper.peers.javafxgraphics.gwt.html.layoutmeasurable.HtmlLayoutMeasurable;
import webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;

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
