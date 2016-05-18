package naga.core.spi.gui.javafx.nodes;

import javafx.beans.property.Property;
import javafx.scene.control.TextField;

/**
 * @author Bruno Salmon
 */
public class FxTextField implements naga.core.spi.gui.nodes.TextField<TextField> {

    private TextField textField;

    public FxTextField() {
        this(new TextField());
    }

    public FxTextField(TextField textField) {
        this.textField = textField;
    }

    @Override
    public TextField unwrapToNativeNode() {
        return textField;
    }

    @Override
    public void requestFocus() {
        textField.requestFocus();
    }

    @Override
    public Property<String> textProperty() {
        return textField.textProperty();
    }

    @Override
    public Property<String> placeholderProperty() {
        return textField.promptTextProperty();
    }
}
