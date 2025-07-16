package dev.webfx.kit.mapper.peers.javafxcontrols.gwtj2cl.html;

import dev.webfx.kit.mapper.peers.javafxcontrols.base.TextAreaPeerBase;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.TextAreaPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.HasNoChildrenPeers;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html.layoutmeasurable.HtmlLayoutMeasurable;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.util.HtmlUtil;
import javafx.scene.control.TextArea;

/**
 * @author Bruno Salmon
 */
public final class HtmlTextAreaPeer
        <N extends TextArea, NB extends TextAreaPeerBase<N, NB, NM>, NM extends TextAreaPeerMixin<N, NB, NM>>

        extends HtmlTextInputControlPeer<N, NB, NM>
        implements TextAreaPeerMixin<N, NB, NM>, HtmlLayoutMeasurable, HasNoChildrenPeers {

    public HtmlTextAreaPeer() {
        super((NB) new TextAreaPeerBase(), HtmlUtil.createTextArea(), "fx-textarea");
        getElement().style.resize = "none"; // To disable the HTML text area resize feature
    }

    @Override
    public void updateWrapText(boolean wrapText) {
        setElementStyleAttribute("word-break", wrapText ? "break-word" : "normal");
    }

    @Override
    public void updatePrefColumnCount(Number prefColumnCount) {
        setElementAttribute("cols", prefColumnCount);
    }

    @Override
    public void updatePrefRowCount(Number prefRowCount) {
        setElementAttribute("rows", prefRowCount);
    }
}
