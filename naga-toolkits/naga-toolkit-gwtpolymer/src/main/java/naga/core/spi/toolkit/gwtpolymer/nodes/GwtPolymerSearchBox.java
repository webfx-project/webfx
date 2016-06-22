package naga.core.spi.toolkit.gwtpolymer.nodes;

import com.vaadin.polymer.paper.widget.PaperInput;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.spi.toolkit.gwt.GwtNode;
import naga.core.spi.toolkit.nodes.SearchBox;

/**
 * @author Bruno Salmon
 */
public class GwtPolymerSearchBox extends GwtNode<PaperInput> implements SearchBox<PaperInput> {

    public GwtPolymerSearchBox() {
        this(new PaperInput());
    }

    public GwtPolymerSearchBox(PaperInput search) {
        super(search);
        search.getPolymerElement().addEventListener("keyup", event -> textProperty.setValue(search.getValue()));
        placeholderProperty.addListener((observable, oldValue, newValue) -> node.setPlaceholder(newValue));
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
