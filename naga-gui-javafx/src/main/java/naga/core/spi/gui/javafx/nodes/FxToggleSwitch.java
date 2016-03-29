package naga.core.spi.gui.javafx.nodes;

import javafx.beans.property.Property;
import naga.core.spi.gui.javafx.FxNode;
import naga.core.spi.gui.javafx.controlsfx.ToggleSwitch;

/**
 * @author Bruno Salmon
 */
public class FxToggleSwitch extends FxNode<ToggleSwitch> implements naga.core.spi.gui.nodes.ToggleSwitch<ToggleSwitch> {

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
