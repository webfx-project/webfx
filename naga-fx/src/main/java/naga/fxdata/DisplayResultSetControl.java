package naga.fxdata;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.fxdata.displaydata.DisplayResultSet;
import javafx.scene.control.Control;
import naga.fxdata.displaydata.HasDisplayResultSetProperty;

/**
 * @author Bruno Salmon
 */
public abstract class DisplayResultSetControl extends Control implements
        HasDisplayResultSetProperty {

    private Property<DisplayResultSet> displayResultSetProperty = new SimpleObjectProperty<DisplayResultSet>() {
        @Override
        protected void invalidated() {
            requestParentLayout();
        }
    };
    @Override
    public Property<DisplayResultSet> displayResultSetProperty() {
        return displayResultSetProperty;
    }

}
