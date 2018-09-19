package emul.com.sun.javafx.scene.control.skin;

import emul.javafx.geometry.Insets;
import emul.javafx.scene.control.Skin;
import emul.javafx.scene.control.TextField;
import emul.javafx.scene.layout.Background;
import webfx.fxkits.core.util.properties.ObservableLists;

/**
 * @author Bruno Salmon
 */
public class ToolkitTextBox extends TextField {

    private final TextField embeddingTextField;

    public ToolkitTextBox(TextField embeddingTextField) {
        this.embeddingTextField = embeddingTextField;
        ObservableLists.bind(getStyleClass(), embeddingTextField.getStyleClass());
        editableProperty().bind(embeddingTextField.editableProperty());
        textProperty().bindBidirectional(embeddingTextField.textProperty());
        promptTextProperty().bind(embeddingTextField.promptTextProperty());
        focusedProperty().addListener((observable, oldValue, newValue) -> embeddingTextField.setFocused(newValue));
        embeddingTextField.focusedProperty().addListener((observable, oldValue, newValue) -> setFocused(newValue));
    }

    public TextField getEmbeddingTextField() {
        return embeddingTextField;
    }

    // Setting the default skin (empty as we rely on the target toolkit for now) but this allows to add decorators for validation
    @Override
    protected Skin<?> createDefaultSkin() {
        return null;
    }

    {
        setPadding(Insets.EMPTY);
        setBackground(Background.EMPTY);
    }

}
