package naga.core.spi.toolkit.hasproperties;

import javafx.beans.property.Property;
import naga.core.ngui.displayresultset.DisplayResultSet;

/**
 * @author Bruno Salmon
 */

public interface HasDisplayResultSetProperty {

    Property<DisplayResultSet> displayResultSetProperty();
    default void setDisplayResultSet(DisplayResultSet selected) { displayResultSetProperty().setValue(selected); }
    default DisplayResultSet getDisplayResultSet() { return displayResultSetProperty().getValue(); }

}
