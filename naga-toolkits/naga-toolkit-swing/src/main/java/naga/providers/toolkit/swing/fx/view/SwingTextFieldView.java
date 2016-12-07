package naga.providers.toolkit.swing.fx.view;

import naga.commons.util.Objects;
import naga.providers.toolkit.swing.util.JPlaceholderTextField;
import naga.providers.toolkit.swing.util.StyleUtil;
import naga.providers.toolkit.swing.util.SwingFonts;
import naga.toolkit.fx.scene.control.TextField;
import naga.toolkit.fx.spi.view.base.TextFieldViewBase;
import naga.toolkit.fx.spi.view.base.TextFieldViewMixin;
import naga.toolkit.fx.scene.text.Font;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingTextFieldView
        extends SwingRegionView<TextField, TextFieldViewBase, TextFieldViewMixin>
        implements TextFieldViewMixin, SwingEmbedComponentView<TextField>, SwingLayoutMeasurable<TextField> {

    private final JPlaceholderTextField swingTextField = new JPlaceholderTextField();

    public SwingTextFieldView() {
        super(new TextFieldViewBase());
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
