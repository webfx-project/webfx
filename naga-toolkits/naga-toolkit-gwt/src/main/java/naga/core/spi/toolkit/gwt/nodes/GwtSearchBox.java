package naga.core.spi.toolkit.gwt.nodes;

import com.google.gwt.user.client.ui.TextBox;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.spi.toolkit.gwt.GwtNode;
import naga.core.spi.toolkit.nodes.SearchBox;

/**
 * @author Bruno Salmon
 */
public class GwtSearchBox extends GwtNode<TextBox> implements SearchBox<TextBox> {

    public GwtSearchBox() {
        this(new TextBox());
    }

    public GwtSearchBox(TextBox search) {
        super(search);
        search.addValueChangeHandler(event -> textProperty.setValue(search.getValue()));
        search.addKeyUpHandler(event -> textProperty.setValue(search.getValue()));
        //placeholderProperty.addListener((observable, oldValue, newValue) -> node.setPlaceholder(newValue));
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
