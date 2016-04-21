package naga.core.spi.gui.cn1.nodes;

import com.codename1.ui.TextField;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.spi.gui.cn1.Cn1Node;
import naga.core.spi.gui.nodes.SearchBox;


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
