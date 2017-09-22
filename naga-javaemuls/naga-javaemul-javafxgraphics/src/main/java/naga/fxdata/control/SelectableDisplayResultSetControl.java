package naga.fxdata.control;

import emul.javafx.beans.property.Property;
import emul.javafx.beans.property.SimpleObjectProperty;
import naga.fxdata.displaydata.DisplaySelection;
import naga.fxdata.displaydata.HasDisplaySelectionProperty;
import naga.fxdata.displaydata.HasSelectionModeProperty;
import naga.fxdata.displaydata.SelectionMode;

import static naga.fxdata.displaydata.SelectionMode.SINGLE;

/**
 * @author Bruno Salmon
 */
public abstract class SelectableDisplayResultSetControl extends DisplayResultSetControl implements
        HasDisplaySelectionProperty,
        HasSelectionModeProperty {

    private Property<DisplaySelection> displaySelectionProperty = new SimpleObjectProperty<>();
    @Override
    public Property<DisplaySelection> displaySelectionProperty() {
        return displaySelectionProperty;
    }

    private Property<SelectionMode> selectionModeProperty = new SimpleObjectProperty<>(SINGLE);
    @Override
    public Property<SelectionMode> selectionModeProperty() {
        return selectionModeProperty;
    }
}
