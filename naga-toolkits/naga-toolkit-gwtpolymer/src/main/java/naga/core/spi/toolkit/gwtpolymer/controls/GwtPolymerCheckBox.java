package naga.core.spi.toolkit.gwtpolymer.controls;

import com.vaadin.polymer.paper.widget.PaperCheckbox;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.spi.toolkit.gwt.node.GwtNode;
import naga.core.spi.toolkit.controls.CheckBox;
import naga.core.spi.toolkit.controls.ToggleSwitch;

/**
 * @author Bruno Salmon
 */
public class GwtPolymerCheckBox extends GwtNode<PaperCheckbox> implements CheckBox<PaperCheckbox>, ToggleSwitch<PaperCheckbox> {

    public GwtPolymerCheckBox() {
        this(new PaperCheckbox());
    }

    public GwtPolymerCheckBox(PaperCheckbox button) {
        super(button);
        selectedProperty.setValue(button.getChecked());
        //Polymer.ready(button.getElement(), o -> {
            textProperty.addListener((observable, oldValue, newValue) -> button.getElement().setInnerHTML(newValue));
            selectedProperty.addListener((observable, oldValue, newValue) -> button.setChecked(newValue));
            button.addChangeHandler(event -> selectedProperty.setValue(button.getChecked()));
        //    return null;
        //});
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
