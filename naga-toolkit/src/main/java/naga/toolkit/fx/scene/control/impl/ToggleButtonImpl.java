package naga.toolkit.fx.scene.control.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.fx.scene.control.ToggleButton;

/**
 * @author Bruno Salmon
 */
public class ToggleButtonImpl extends ButtonBaseImpl implements ToggleButton {

    private final Property<Boolean> selectedProperty = new SimpleObjectProperty<>(false);
    @Override
    public Property<Boolean> selectedProperty() {
        return selectedProperty;
    }
}
