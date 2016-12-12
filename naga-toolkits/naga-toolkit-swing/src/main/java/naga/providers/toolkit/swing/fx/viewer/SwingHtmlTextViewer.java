package naga.providers.toolkit.swing.fx.viewer;

import naga.toolkit.fx.ext.control.HtmlText;
import naga.toolkit.fx.spi.viewer.base.HtmlTextViewerBase;
import naga.toolkit.fx.spi.viewer.base.HtmlTextViewerMixin;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingHtmlTextViewer
        <N extends HtmlText, NV extends HtmlTextViewerBase<N, NV, NM>, NM extends HtmlTextViewerMixin<N, NV, NM>>
        extends SwingRegionViewer<N, NV, NM>
        implements HtmlTextViewerMixin<N, NV, NM>, SwingLayoutMeasurable<N> {

    private final JEditorPane editorPane = new JEditorPane();

    public SwingHtmlTextViewer() {
        this((NV) new HtmlTextViewerBase());
    }

    SwingHtmlTextViewer(NV base) {
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
