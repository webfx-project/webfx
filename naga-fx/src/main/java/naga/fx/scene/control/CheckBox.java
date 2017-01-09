package naga.fx.scene.control;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.fx.event.ActionEvent;
import naga.fx.properties.markers.HasSelectedProperty;

/**
 * @author Bruno Salmon
 */
public class CheckBox extends ButtonBase implements
        HasSelectedProperty {

    private final Property<Boolean> selectedProperty = new SimpleObjectProperty<>(false);
    @Override
    public Property<Boolean> selectedProperty() {
        return selectedProperty;
    }

    /**
     * Toggles the state of the {@code CheckBox}. If allowIndeterminate is
     * true, then each invocation of this function will advance the CheckBox
     * through the states checked, unchecked, and undefined. If
     * allowIndeterminate is false, then the CheckBox will only cycle through
     * the checked and unchecked states, and forcing indeterminate to equal to
     * false.
     */
    @Override public void fire() {
        if (!isDisabled()) {
/*            if (isAllowIndeterminate()) {
                if (!isSelected() && !isIndeterminate()) {
                    setIndeterminate(true);
                } else if (isSelected() && !isIndeterminate()) {
                    setSelected(false);
                } else if (isIndeterminate()) {
                    setSelected(true);
                    setIndeterminate(false);
                }
            } else*/ {
                setSelected(!isSelected());
                //setIndeterminate(false);
            }
            fireEvent(new ActionEvent());
        }
    }

}
