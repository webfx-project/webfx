package naga.providers.toolkit.swing.fx.viewer;

import naga.commons.util.Objects;
import naga.providers.toolkit.swing.util.JPlaceholderTextField;
import naga.providers.toolkit.swing.util.StyleUtil;
import naga.providers.toolkit.swing.util.SwingFonts;
import naga.toolkit.fx.scene.control.TextField;
import naga.toolkit.fx.scene.text.Font;
import naga.toolkit.fx.spi.viewer.base.TextFieldViewerBase;
import naga.toolkit.fx.spi.viewer.base.TextFieldViewerMixin;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingTextFieldViewer
        <N extends TextField, NV extends TextFieldViewerBase<N, NV, NM>, NM extends TextFieldViewerMixin<N, NV, NM>>

        extends SwingRegionViewer<N, NV, NM>
        implements TextFieldViewerMixin<N, NV, NM>, SwingEmbedComponentViewer<N>, SwingLayoutMeasurable<N> {

    private final JPlaceholderTextField swingTextField = new JPlaceholderTextField();

    public SwingTextFieldViewer() {
        this((NV) new TextFieldViewerBase());
    }

    public SwingTextFieldViewer(NV base) {
        super(base);
        swingTextField.setPreferredSize(new Dimension(200, (int) swingTextField.getMinimumSize().getHeight()));
        swingTextField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { updateNodeText(); }
            public void removeUpdate(DocumentEvent e) { updateNodeText(); }
            public void insertUpdate(DocumentEvent e) { updateNodeText(); }
        });
    }

    @Override
    public JComponent getSwingComponent() {
        return swingTextField;
    }

    @Override
    public void updateFont(Font font) {
        swingTextField.setFont(font != null ? SwingFonts.toSwingFont(font) : StyleUtil.getFont(false, false));
    }

    private void updateNodeText() {
        getNode().setText(swingTextField.getText());
    }

    @Override
    public void updateText(String text) {
        // Checking the text has really changed (Swing raises an exception if setText() is called during a document change notification)
        if (!Objects.areEquals(text, swingTextField.getText()))
            swingTextField.setText(text);
    }

    @Override
    public void updatePrompt(String prompt) {
        swingTextField.setPlaceholder(prompt);
    }
}
