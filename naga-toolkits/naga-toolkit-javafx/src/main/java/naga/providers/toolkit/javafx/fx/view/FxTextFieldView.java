package naga.providers.toolkit.javafx.fx.view;

import naga.providers.toolkit.javafx.util.FxFonts;
import naga.toolkit.fx.scene.control.TextField;
import naga.toolkit.fx.scene.text.Font;
import naga.toolkit.fx.spi.view.base.TextFieldViewBase;
import naga.toolkit.fx.spi.view.base.TextFieldViewMixin;

/**
 * @author Bruno Salmon
 */
public class FxTextFieldView
        extends FxTextInputControlView<javafx.scene.control.TextField, TextField, TextFieldViewBase, TextFieldViewMixin>
        implements TextFieldViewMixin, FxLayoutMeasurable {

    public FxTextFieldView() {
        super(new TextFieldViewBase());
    }

    @Override
    javafx.scene.control.TextField createFxNode() {
        javafx.scene.control.TextField textField = new javafx.scene.control.TextField();
        textField.textProperty().addListener((observable, oldValue, newValue) -> updateNodeText(newValue));
        return textField;
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
        getFxNode().setText(text);
    }

    @Override
    public void updatePrompt(String prompt) {
        getFxNode().setPromptText(prompt);
    }
}
