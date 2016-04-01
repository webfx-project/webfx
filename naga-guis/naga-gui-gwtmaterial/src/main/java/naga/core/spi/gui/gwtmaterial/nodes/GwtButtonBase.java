package naga.core.spi.gui.gwtmaterial.nodes;

import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.HasValue;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.spi.gui.gwtmaterial.GwtNode;

/**
 * @author Bruno Salmon
 */
public class GwtButtonBase<N extends ButtonBase> extends GwtNode<N> implements naga.core.spi.gui.nodes.ButtonBase<N> {

    public GwtButtonBase(N button) {
        super(button);
        textProperty.setValue(button.getText());
        textProperty.addListener((observable, oldValue, newValue) -> button.setText(newValue));
        if (button instanceof HasValue) {
            HasValue<Boolean> selectedValue = (HasValue<Boolean>) button;
            selectedProperty.setValue(selectedValue.getValue());
            button.addClickHandler(event -> selectedProperty.setValue(selectedValue.getValue()));
            selectedProperty.addListener((observable, oldValue, newValue) -> selectedValue.setValue(newValue));
        }
    }

    private final Property<Boolean> selectedProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Boolean> selectedProperty() {
        return selectedProperty;
    }

    private final Property<String> textProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> textProperty() {
        return textProperty;
    }
}
