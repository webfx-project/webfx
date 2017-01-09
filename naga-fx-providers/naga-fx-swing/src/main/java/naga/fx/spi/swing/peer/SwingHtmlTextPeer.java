package naga.fx.spi.swing.peer;

import naga.fxdata.control.HtmlText;
import naga.fxdata.spi.peer.base.HtmlTextPeerBase;
import naga.fxdata.spi.peer.base.HtmlTextPeerMixin;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingHtmlTextPeer
        <N extends HtmlText, NB extends HtmlTextPeerBase<N, NB, NM>, NM extends HtmlTextPeerMixin<N, NB, NM>>
        extends SwingRegionPeer<N, NB, NM>
        implements HtmlTextPeerMixin<N, NB, NM>, SwingLayoutMeasurable<N> {

    private final JEditorPane editorPane = new JEditorPane();

    public SwingHtmlTextPeer() {
        this((NB) new HtmlTextPeerBase());
    }

    SwingHtmlTextPeer(NB base) {
        super(base);
        editorPane.setContentType("text/html");
        editorPane.setEditable(false);
    }

    @Override
    public JComponent getSwingComponent() {
        return editorPane;
    }

    @Override
    public void updateText(String text) {
        editorPane.setText(text);
    }
}
