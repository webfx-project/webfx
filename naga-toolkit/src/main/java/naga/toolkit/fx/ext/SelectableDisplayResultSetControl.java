package naga.toolkit.fx.ext;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.display.DisplaySelection;
import naga.toolkit.properties.markers.HasDisplaySelectionProperty;
import naga.toolkit.properties.markers.HasSelectionModeProperty;
import naga.toolkit.properties.markers.SelectionMode;

import static naga.toolkit.properties.markers.SelectionMode.SINGLE;

/**
 * @author Bruno Salmon
 */
public abstract class SelectableDisplayResultSetControl extends DisplayResultSetControl implements HasDisplaySelectionProperty,
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
