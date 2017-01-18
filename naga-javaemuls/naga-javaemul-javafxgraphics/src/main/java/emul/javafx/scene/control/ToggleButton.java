package emul.javafx.scene.control;

import emul.javafx.beans.property.Property;
import emul.javafx.beans.property.SimpleObjectProperty;
import emul.javafx.event.ActionEvent;
import naga.fx.properties.markers.HasSelectedProperty;

/**
 * @author Bruno Salmon
 */
public abstract class ToggleButton extends ButtonBase implements
        HasSelectedProperty {

    private final Property<Boolean> selectedProperty = new SimpleObjectProperty<>(false);
    @Override
    public Property<Boolean> selectedProperty() {
        return selectedProperty;
    }

    @Override public void fire() {
        // TODO (aruiz): if (!isReadOnly(isSelected()) {
        if (!isDisabled()) {
            setSelected(!isSelected());
            fireEvent(new ActionEvent());
        }
    }

}
