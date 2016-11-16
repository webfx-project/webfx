package naga.providers.toolkit.gwtmaterial.nodes.controls;

import gwt.material.design.client.ui.MaterialSearch;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.gwt.nodes.GwtNode;
import naga.toolkit.spi.nodes.controls.SearchBox;

/**
 * @author Bruno Salmon
 */
public class GwtMaterialSearchBox extends GwtNode<MaterialSearch> implements SearchBox {

    public GwtMaterialSearchBox() {
        this(new MaterialSearch());
    }

    public GwtMaterialSearchBox(MaterialSearch search) {
        super(search);
        search.addValueChangeHandler(event -> textProperty.setValue(search.getValue()));
        search.addKeyUpHandler(event -> textProperty.setValue(search.getValue()));
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
