package naga.fx.spi.swing.peer;

import naga.commons.util.Objects;
import emul.javafx.scene.control.TextField;
import emul.javafx.scene.text.Font;
import naga.fx.spi.peer.base.TextFieldPeerBase;
import naga.fx.spi.peer.base.TextFieldPeerMixin;
import naga.fx.spi.swing.util.JPlaceholderTextField;
import naga.fx.spi.swing.util.StyleUtil;
import naga.fx.spi.swing.util.SwingFonts;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * @author Bruno Salmon
 */
public class SwingTextFieldPeer
        <N extends TextField, NB extends TextFieldPeerBase<N, NB, NM>, NM extends TextFieldPeerMixin<N, NB, NM>>

        extends SwingRegionPeer<N, NB, NM>
        implements TextFieldPeerMixin<N, NB, NM>, SwingEmbedComponentPeer<N>, SwingLayoutMeasurable<N> {

    private final JPlaceholderTextField swingTextField = new JPlaceholderTextField();

    public SwingTextFieldPeer() {
        this((NB) new TextFieldPeerBase());
    }

    public SwingTextFieldPeer(NB base) {
        super(base);
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
