package naga.providers.toolkit.javafx.nodes.controls;

import javafx.beans.property.Property;
import javafx.scene.control.RadioButton;

/**
 * @author Bruno Salmon
 */
public class FxRadioButton extends FxButtonBase<RadioButton> implements naga.toolkit.spi.nodes.controls.RadioButton<RadioButton> {

    public FxRadioButton() {
        this(createRadioButton());
    }

    public FxRadioButton(RadioButton radioButton) {
        super(radioButton);
    }

    private static RadioButton createRadioButton() {
        return new RadioButton();
    }

    @Override
    public Property<Boolean> selectedProperty() {
        return node.selectedProperty();
    }
}
