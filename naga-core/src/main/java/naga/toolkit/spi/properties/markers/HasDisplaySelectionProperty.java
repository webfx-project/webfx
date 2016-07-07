package naga.toolkit.spi.properties.markers;

import javafx.beans.property.Property;
import naga.toolkit.spi.display.DisplaySelection;

/**
 * @author Bruno Salmon
 */

public interface HasDisplaySelectionProperty {

    Property<DisplaySelection> displaySelectionProperty();
    default void setDisplaySelection(DisplaySelection displaySelection) { displaySelectionProperty().setValue(displaySelection); }
    default DisplaySelection getDisplaySelection() { return displaySelectionProperty().getValue(); }

}
