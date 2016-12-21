package naga.toolkit.fxdata;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.fxdata.displaydata.DisplaySelection;
import naga.toolkit.fxdata.displaydata.HasDisplaySelectionProperty;
import naga.toolkit.fxdata.displaydata.HasSelectionModeProperty;
import naga.toolkit.fxdata.displaydata.SelectionMode;

import static naga.toolkit.fxdata.displaydata.SelectionMode.SINGLE;

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
