package naga.toolkit.fxdata.displaydata;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasSelectionModeProperty {

    Property<SelectionMode> selectionModeProperty();
    default void setSelectionMode(SelectionMode selectionMode) { selectionModeProperty().setValue(selectionMode); }
    default SelectionMode getSelectionMode() { return selectionModeProperty().getValue(); }

}
