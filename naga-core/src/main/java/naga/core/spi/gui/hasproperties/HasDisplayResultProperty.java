package naga.core.spi.gui.hasproperties;

import javafx.beans.property.Property;
import naga.core.ngui.displayresult.DisplayResult;

/**
 * @author Bruno Salmon
 */

public interface HasDisplayResultProperty {

    Property<DisplayResult> displayResultProperty();
    default void setDisplayResult(DisplayResult selected) { displayResultProperty().setValue(selected); }
    default DisplayResult getDisplayResult() { return displayResultProperty().getValue(); }

}
