package naga.core.spi.toolkit.javafx.nodes;

import javafx.beans.property.Property;
import naga.core.spi.toolkit.javafx.FxNode;
import naga.core.spi.toolkit.javafx.controlsfx.ToggleSwitch;

/**
 * @author Bruno Salmon
 */
public class FxToggleSwitch extends FxNode<ToggleSwitch> implements naga.core.spi.toolkit.nodes.ToggleSwitch<ToggleSwitch> {

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
