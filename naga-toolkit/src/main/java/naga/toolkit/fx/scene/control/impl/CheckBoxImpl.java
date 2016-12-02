package naga.toolkit.fx.scene.control.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.fx.scene.control.CheckBox;

/**
 * @author Bruno Salmon
 */
public class CheckBoxImpl extends ButtonBaseImpl implements CheckBox {

    private final Property<Boolean> selectedProperty = new SimpleObjectProperty<>(false);
    @Override
    public Property<Boolean> selectedProperty() {
        return selectedProperty;
    }
}
