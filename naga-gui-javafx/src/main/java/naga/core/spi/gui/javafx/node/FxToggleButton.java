package naga.core.spi.gui.javafx.node;

import javafx.beans.property.Property;
import javafx.scene.control.ToggleButton;

/**
 * @author Bruno Salmon
 */
public class FxToggleButton extends FxButtonBase<ToggleButton> implements naga.core.spi.gui.node.ToggleButton<ToggleButton> {

    public FxToggleButton() {
        this(new ToggleButton());
    }

    public FxToggleButton(ToggleButton toggleButton) {
        super(toggleButton);
    }

    @Override
    public Property<Boolean> selectedProperty() {
        return node.selectedProperty();
    }

}
