package naga.fx.spi.swing.fx.viewer;

import naga.fxdata.control.HtmlText;
import naga.fxdata.spi.viewer.base.HtmlTextViewerBase;
import naga.fxdata.spi.viewer.base.HtmlTextViewerMixin;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingHtmlTextViewer
        <N extends HtmlText, NB extends HtmlTextViewerBase<N, NB, NM>, NM extends HtmlTextViewerMixin<N, NB, NM>>
        extends SwingRegionViewer<N, NB, NM>
        implements HtmlTextViewerMixin<N, NB, NM>, SwingLayoutMeasurable<N> {

    private final JEditorPane editorPane = new JEditorPane();

    public SwingHtmlTextViewer() {
        this((NB) new HtmlTextViewerBase());
    }

    SwingHtmlTextViewer(NB base) {
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
