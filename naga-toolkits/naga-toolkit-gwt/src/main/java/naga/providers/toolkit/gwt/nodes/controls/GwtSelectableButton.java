package naga.providers.toolkit.gwt.nodes.controls;

import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.HasValue;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.spi.nodes.controls.SelectableButton;

/**
 * @author Bruno Salmon
 */
public class GwtSelectableButton<N extends ButtonBase> extends GwtButtonBase<N> implements SelectableButton<N> {

    public GwtSelectableButton(N button) {
        super(button);
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


}
