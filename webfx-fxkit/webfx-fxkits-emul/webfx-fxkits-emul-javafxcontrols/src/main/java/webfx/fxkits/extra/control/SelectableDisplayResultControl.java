package webfx.fxkits.extra.control;

import emul.javafx.beans.property.Property;
import emul.javafx.beans.property.SimpleObjectProperty;
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
