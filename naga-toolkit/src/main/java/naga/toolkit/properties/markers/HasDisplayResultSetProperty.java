package naga.toolkit.properties.markers;

import javafx.beans.property.Property;
import naga.toolkit.display.DisplayResultSet;

/**
 * @author Bruno Salmon
 */

public interface HasDisplayResultSetProperty {

    Property<DisplayResultSet> displayResultSetProperty();
    default void setDisplayResultSet(DisplayResultSet selected) { displayResultSetProperty().setValue(selected); }
    default DisplayResultSet getDisplayResultSet() { return displayResultSetProperty().getValue(); }

}
