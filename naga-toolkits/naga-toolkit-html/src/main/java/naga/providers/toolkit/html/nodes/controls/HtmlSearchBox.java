package naga.providers.toolkit.html.nodes.controls;

import elemental2.HTMLInputElement;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.providers.toolkit.html.nodes.HtmlNode;
import naga.toolkit.spi.nodes.controls.SearchBox;

/**
 * @author Bruno Salmon
 */
public class HtmlSearchBox extends HtmlNode<HTMLInputElement> implements SearchBox {

    public HtmlSearchBox() {
        this(HtmlUtil.createTextInput());
    }

    public HtmlSearchBox(HTMLInputElement search) {
        super(search);
        search.oninput = a -> {
            textProperty.setValue(search.value);
            return null;
        };
        placeholderProperty.addListener((observable, oldValue, newValue) -> node.placeholder = newValue);
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
