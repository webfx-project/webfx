package naga.toolkit.providers.cn1.nodes.controls;

import com.codename1.ui.TextField;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.providers.cn1.nodes.Cn1Node;
import naga.toolkit.spi.nodes.controls.SearchBox;


/**
 * @author Bruno Salmon
 */
public class Cn1SearchBox extends Cn1Node<TextField> implements SearchBox<TextField> {

    public Cn1SearchBox() {
        this(new TextField());
    }

    public Cn1SearchBox(TextField search) {
        super(search);
        search.addDataChangeListener((type, index) -> textProperty.setValue(search.getText()));
        placeholderProperty.addListener((observable, oldValue, newValue) -> node.setHint(newValue));
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
