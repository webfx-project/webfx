package naga.toolkit.providers.javafx.nodes.controls;

import javafx.beans.property.Property;
import naga.toolkit.providers.javafx.nodes.controlsfx.ToggleSwitch;
import naga.toolkit.providers.javafx.nodes.FxNode;

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
