package naga.core.spi.toolkit.pivot.controls;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.spi.toolkit.controls.SearchBox;
import naga.core.spi.toolkit.pivot.node.PivotNode;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.TextInputContentListener;

/**
 * @author Bruno Salmon
 */
public class PivotSearchBox extends PivotNode<TextInput> implements SearchBox<TextInput> {

    public PivotSearchBox() {
        this(new TextInput());
    }

    public PivotSearchBox(TextInput search) {
        super(search);
        search.getTextInputContentListeners().add(new TextInputContentListener.Adapter() {
            @Override
            public void textChanged(TextInput textInput) {
                textProperty.setValue(textInput.getText());
            }
        });
        placeholderProperty.addListener((observable, oldValue, newValue) -> node.setPrompt(newValue));
    }

    private final Property<String> textProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> textProperty() {
        return textProperty;
    }

    private final Property<String> placeholderProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> placeholderProperty() {
        return placeholderProperty;
    }
}
