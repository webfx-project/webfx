package naga.toolkit.fxdata.displaydata;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */

public interface HasDisplayResultSetProperty {

    Property<DisplayResultSet> displayResultSetProperty();
    default void setDisplayResultSet(DisplayResultSet selected) { displayResultSetProperty().setValue(selected); }
    default DisplayResultSet getDisplayResultSet() { return displayResultSetProperty().getValue(); }

}
