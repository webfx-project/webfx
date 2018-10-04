package webfx.fxkit.extra.control;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import webfx.fxkit.extra.displaydata.HasDisplaySelectionProperty;
import webfx.fxkit.extra.displaydata.DisplaySelection;
import webfx.fxkit.extra.displaydata.HasSelectionModeProperty;
import webfx.fxkit.extra.displaydata.SelectionMode;

import static webfx.fxkit.extra.displaydata.SelectionMode.SINGLE;

/**
 * @author Bruno Salmon
 */
public abstract class SelectableDisplayResultControl extends DisplayResultControl implements
        HasDisplaySelectionProperty,
        HasSelectionModeProperty {

    private final Property<DisplaySelection> displaySelectionProperty = new SimpleObjectProperty<>();
    @Override
    public Property<DisplaySelection> displaySelectionProperty() {
        return displaySelectionProperty;
    }

    private final Property<SelectionMode> selectionModeProperty = new SimpleObjectProperty<>(SINGLE);
    @Override
    public Property<SelectionMode> selectionModeProperty() {
        return selectionModeProperty;
    }
}
