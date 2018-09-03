package webfx.fxkit.javafx.peer;

import javafx.geometry.Pos;
import webfx.fxkits.core.spi.peer.base.TextFieldPeerBase;
import webfx.fxkits.core.spi.peer.base.TextFieldPeerMixin;
import webfx.platforms.core.util.Objects;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

/**
 * @author Bruno Salmon
 */
public class FxTextFieldPeer
        <FxN extends javafx.scene.control.TextField, N extends TextField, NB extends TextFieldPeerBase<N, NB, NM>, NM extends TextFieldPeerMixin<N, NB, NM>>

        extends FxTextInputControlPeer<FxN, N, NB, NM>
        implements TextFieldPeerMixin<N, NB, NM>, FxLayoutMeasurable {

    public FxTextFieldPeer() {
        super((NB) new TextFieldPeerBase());
    }

    @Override
    protected FxN createFxNode() {
        javafx.scene.control.TextField textField = new javafx.scene.control.TextField();
        textField.textProperty().addListener((observable, oldValue, newValue) -> updateNodeText(newValue));
        return (FxN) textField;
    }

    @Override
    public void updateFont(Font font) {
        if (font != null)
            getFxNode().setFont(font);
    }

    private void updateNodeText(String text) {
        getNode().setText(text);
    }

    @Override
    public void updateText(String text) {
        if (!Objects.areEquals(text, getFxNode().getText())) // to avoid caret position reset while typing
            getFxNode().setText(text);
    }

    @Override
    public void updatePrompt(String prompt) {
        getFxNode().setPromptText(prompt);
    }

    @Override
    public void updateEditable(Boolean editable) {
        getFxNode().setEditable(editable);
    }

    @Override
    public void updateAlignment(Pos alignment) {
        getFxNode().setAlignment(alignment);
    }
}
