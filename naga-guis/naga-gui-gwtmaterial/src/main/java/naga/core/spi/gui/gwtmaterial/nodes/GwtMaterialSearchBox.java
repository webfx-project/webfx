package naga.core.spi.gui.gwtmaterial.nodes;

import gwt.material.design.client.ui.MaterialSearch;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.spi.gui.gwtmaterial.GwtNode;
import naga.core.spi.gui.nodes.SearchBox;

/**
 * @author Bruno Salmon
 */
public class GwtMaterialSearchBox extends GwtNode<MaterialSearch> implements SearchBox<MaterialSearch> {

    public GwtMaterialSearchBox() {
        this(new MaterialSearch());
    }

    public GwtMaterialSearchBox(MaterialSearch search) {
        super(search);
        search.addValueChangeHandler(event -> textProperty.setValue(search.getValue()));
        search.addKeyUpHandler(event -> textProperty.setValue(search.getValue()));
    }

    private final Property<String> textProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> textProperty() {
        return textProperty;
    }
}
