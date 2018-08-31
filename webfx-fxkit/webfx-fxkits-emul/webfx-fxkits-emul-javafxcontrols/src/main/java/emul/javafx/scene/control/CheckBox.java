package emul.javafx.scene.control;

import emul.com.sun.javafx.scene.control.skin.CheckBoxSkin;
import emul.javafx.beans.property.BooleanProperty;
import emul.javafx.beans.property.SimpleBooleanProperty;
import emul.javafx.event.ActionEvent;
import emul.javafx.scene.Cursor;
import webfx.fx.properties.markers.HasSelectedProperty;

/**
 * @author Bruno Salmon
 */
public class CheckBox extends ButtonBase implements
        HasSelectedProperty {

    private final BooleanProperty selectedProperty = new SimpleBooleanProperty(false);
    @Override
    public BooleanProperty selectedProperty() {
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

    /**
     * Checkbox uses DEFAULT as the default value for cursor.
     * This method provides a way for css to get the correct initial value.
     * @treatAsPrivate implementation detail
     */
    @Deprecated @Override
    protected /*do not make final*/ Cursor impl_cssGetCursorInitialValue() {
        return Cursor.DEFAULT;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new CheckBoxSkin(this);
    }

/*
    // We continue to use the target toolkit layout measurable even if there is a skin
    @Override
    public boolean shouldUseLayoutMeasurable() {
        return true;
    }
*/

}
