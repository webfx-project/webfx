package javafx.scene.control.skin;

import dev.webfx.kit.registry.javafxcontrols.JavaFxControlsRegistry;
import dev.webfx.kit.util.properties.FXProperties;
import dev.webfx.kit.util.properties.ObservableLists;
import javafx.geometry.Insets;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;

/**
 * @author Bruno Salmon
 */
public class ToolkitTextBox extends TextField { // WebFX specific class (not part of JavaFX)

    private final TextField embeddingTextField;

    public ToolkitTextBox(TextField embeddingTextField) {
        this.embeddingTextField = embeddingTextField;
        ObservableLists.bind(getStyleClass(), embeddingTextField.getStyleClass());
        editableProperty().bind(embeddingTextField.editableProperty());
        textProperty().bindBidirectional(embeddingTextField.textProperty());
        promptTextProperty().bind(embeddingTextField.promptTextProperty());
        FXProperties.runNowAndOnPropertiesChange(() -> setDisabled(embeddingTextField.isDisabled()), embeddingTextField.disabledProperty());
        // We set focusTraversal to false so that when the user clicks on that ToolkitTextBox on the DOM side to get the
        // focus, the focus mapping from HTML to JavaFX will pass the focus to embeddingTextField on the JavaFX side
        // and not ToolkitTextBox. Otherwise, giving the focus to ToolkitTextBox would confuse an application code
        // testing if the text field is focused.
        setFocusTraversable(false); // see HtmlSvgNodePeer.getFocusableNode()
        // The following code has been commented as it doesn't look necessary (to remove definitely if no issue).
        //FXProperties.runNowAndOnPropertiesChange(() -> setFocused(embeddingTextField.isFocused()), embeddingTextField.focusedProperty());
        //focusedProperty().addListener((observable, oldValue, newValue) -> embeddingTextField.setFocused(newValue));
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
