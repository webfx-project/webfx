package naga.providers.toolkit.javafx.fx.viewer;

import naga.commons.util.Objects;
import naga.providers.toolkit.javafx.util.FxFonts;
import naga.toolkit.fx.scene.control.TextField;
import naga.toolkit.fx.scene.text.Font;
import naga.toolkit.fx.spi.viewer.base.TextFieldViewerBase;
import naga.toolkit.fx.spi.viewer.base.TextFieldViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxTextFieldViewer
        <FxN extends javafx.scene.control.TextField, N extends TextField, NV extends TextFieldViewerBase<N, NV, NM>, NM extends TextFieldViewerMixin<N, NV, NM>>

        extends FxTextInputControlViewer<FxN, N, NV, NM>
        implements TextFieldViewerMixin<N, NV, NM>, FxLayoutMeasurable {

    public FxTextFieldViewer() {
        super((NV) new TextFieldViewerBase());
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
