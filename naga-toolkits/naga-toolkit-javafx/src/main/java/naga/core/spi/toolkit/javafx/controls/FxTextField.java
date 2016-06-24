package naga.core.spi.toolkit.javafx.controls;

import javafx.beans.property.Property;
import javafx.scene.control.TextField;
import naga.core.spi.toolkit.javafx.node.FxNode;

/**
 * @author Bruno Salmon
 */
public class FxTextField extends FxNode<TextField> implements naga.core.spi.toolkit.controls.TextField<TextField> {

    public FxTextField() {
        this(new TextField());
    }

    public FxTextField(TextField textField) {
        super(textField);
    }

    @Override
    public Property<String> textProperty() {
        return node.textProperty();
    }

    @Override
    public Property<String> placeholderProperty() {
        return node.promptTextProperty();
    }
}
