package naga.core.spi.toolkit.javafx.controls;

import javafx.beans.property.Property;
import naga.core.spi.toolkit.javafx.node.FxNode;
import naga.core.spi.toolkit.javafx.controlsfx.ToggleSwitch;

/**
 * @author Bruno Salmon
 */
public class FxToggleSwitch extends FxNode<ToggleSwitch> implements naga.core.spi.toolkit.controls.ToggleSwitch<ToggleSwitch> {

    public FxToggleSwitch() {
        this(new ToggleSwitch());
    }

    public FxToggleSwitch(ToggleSwitch toggleButton) {
        super(toggleButton);
    }

    @Override
    public Property<Boolean> selectedProperty() {
        return node.selectedProperty();
    }

    @Override
    public Property<String> textProperty() {
        return node.textProperty();
    }
}
