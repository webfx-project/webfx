package naga.core.spi.toolkit.hasproperties;

import javafx.beans.property.Property;
import naga.core.ngui.displayselection.DisplaySelection;

/**
 * @author Bruno Salmon
 */

public interface HasDisplaySelectionProperty {

    Property<DisplaySelection> displaySelectionProperty();
    default void setDisplaySelection(DisplaySelection displaySelection) { displaySelectionProperty().setValue(displaySelection); }
    default DisplaySelection getDisplaySelection() { return displaySelectionProperty().getValue(); }

}
