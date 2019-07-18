package webfx.fxkit.extra.controls.displaydata;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import webfx.fxkit.extra.displaydata.DisplaySelection;
import webfx.fxkit.extra.displaydata.HasDisplaySelectionProperty;
import webfx.fxkit.extra.displaydata.HasSelectionModeProperty;
import webfx.fxkit.extra.displaydata.SelectionMode;

import static webfx.fxkit.extra.displaydata.SelectionMode.MULTIPLE;

/**
 * @author Bruno Salmon
 */
public abstract class SelectableDisplayResultControl extends DisplayResultControl implements
        HasDisplaySelectionProperty,
        HasSelectionModeProperty {

    private final ObjectProperty<DisplaySelection> displaySelectionProperty = new SimpleObjectProperty<>();
    @Override
    public ObjectProperty<DisplaySelection> displaySelectionProperty() {
        return displaySelectionProperty;
    }

    private final ObjectProperty<SelectionMode> selectionModeProperty = new SimpleObjectProperty<>(MULTIPLE);
    @Override
    public ObjectProperty<SelectionMode> selectionModeProperty() {
        return selectionModeProperty;
    }
}
