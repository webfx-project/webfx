package webfx.fxdata.control;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import webfx.fxdata.displaydata.DisplaySelection;
import webfx.fxdata.displaydata.HasDisplaySelectionProperty;
import webfx.fxdata.displaydata.HasSelectionModeProperty;
import webfx.fxdata.displaydata.SelectionMode;

import static webfx.fxdata.displaydata.SelectionMode.SINGLE;

/**
 * @author Bruno Salmon
 */
public abstract class SelectableDisplayResultControl extends DisplayResultControl implements
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
