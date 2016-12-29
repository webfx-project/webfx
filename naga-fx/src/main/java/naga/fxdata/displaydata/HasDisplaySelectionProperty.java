package naga.fxdata.displaydata;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */

public interface HasDisplaySelectionProperty {

    Property<DisplaySelection> displaySelectionProperty();
    default void setDisplaySelection(DisplaySelection displaySelection) { displaySelectionProperty().setValue(displaySelection); }
    default DisplaySelection getDisplaySelection() { return displaySelectionProperty().getValue(); }

}
