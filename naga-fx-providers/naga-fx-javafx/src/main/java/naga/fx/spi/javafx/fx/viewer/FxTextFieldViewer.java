package naga.fx.spi.javafx.fx.viewer;

import naga.commons.util.Objects;
import naga.fx.spi.javafx.util.FxFonts;
import naga.fx.scene.control.TextField;
import naga.fx.scene.text.Font;
import naga.fx.spi.viewer.base.TextFieldViewerBase;
import naga.fx.spi.viewer.base.TextFieldViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxTextFieldViewer
        <FxN extends javafx.scene.control.TextField, N extends TextField, NB extends TextFieldViewerBase<N, NB, NM>, NM extends TextFieldViewerMixin<N, NB, NM>>

        extends FxTextInputControlViewer<FxN, N, NB, NM>
        implements TextFieldViewerMixin<N, NB, NM>, FxLayoutMeasurable {

    public FxTextFieldViewer() {
        super((NB) new TextFieldViewerBase());
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
            getFxNode().setFont(FxFonts.toFxFont(font));
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
}
