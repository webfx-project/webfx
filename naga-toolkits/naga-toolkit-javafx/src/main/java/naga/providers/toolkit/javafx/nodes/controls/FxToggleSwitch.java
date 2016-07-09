package naga.providers.toolkit.javafx.nodes.controls;

import javafx.beans.property.Property;
import naga.providers.toolkit.javafx.nodes.FxNode;
import naga.providers.toolkit.javafx.nodes.controlsfx.ToggleSwitch;

/**
 * @author Bruno Salmon
 */
public class FxToggleSwitch extends FxNode<ToggleSwitch> implements naga.toolkit.spi.nodes.controls.ToggleSwitch<ToggleSwitch> {

    public FxToggleSwitch() {
        this(createToggleSwitch());
    }

    public FxToggleSwitch(ToggleSwitch toggleButton) {
        super(toggleButton);
    }

    private static ToggleSwitch createToggleSwitch() {
        return new ToggleSwitch();
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
