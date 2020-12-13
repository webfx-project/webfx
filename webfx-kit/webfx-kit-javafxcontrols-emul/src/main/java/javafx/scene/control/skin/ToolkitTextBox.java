package javafx.scene.control.skin;

import javafx.geometry.Insets;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import dev.webfx.kit.util.properties.ObservableLists;
import dev.webfx.kit.registry.javafxcontrols.JavaFxControlsRegistry;

/**
 * @author Bruno Salmon
 */
public class ToolkitTextBox extends TextField { // WebFx specific class (not part of JavaFx)

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

    static {
        JavaFxControlsRegistry.registerToolkitTextBox();
    }

}
