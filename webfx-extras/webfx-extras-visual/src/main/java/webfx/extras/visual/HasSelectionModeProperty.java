package webfx.extras.visual;

import javafx.beans.property.ObjectProperty;

/**
 * @author Bruno Salmon
 */
public interface HasSelectionModeProperty {

    ObjectProperty<SelectionMode> selectionModeProperty();
    default void setSelectionMode(SelectionMode selectionMode) { selectionModeProperty().setValue(selectionMode); }
    default SelectionMode getSelectionMode() { return selectionModeProperty().getValue(); }

}
