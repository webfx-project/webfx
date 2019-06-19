package webfx.fxkit.extra.displaydata;

import javafx.beans.property.ObjectProperty;

/**
 * @author Bruno Salmon
 */

public interface HasDisplaySelectionProperty {

    ObjectProperty<DisplaySelection> displaySelectionProperty();
    default void setDisplaySelection(DisplaySelection displaySelection) { displaySelectionProperty().setValue(displaySelection); }
    default DisplaySelection getDisplaySelection() { return displaySelectionProperty().getValue(); }

}
