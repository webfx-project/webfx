package webfx.fxkits.extra.control;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import webfx.fxkits.extra.displaydata.DisplaySelection;
import webfx.fxkits.extra.displaydata.HasDisplaySelectionProperty;
import webfx.fxkits.extra.displaydata.HasSelectionModeProperty;
import webfx.fxkits.extra.displaydata.SelectionMode;

import static webfx.fxkits.extra.displaydata.SelectionMode.SINGLE;

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
