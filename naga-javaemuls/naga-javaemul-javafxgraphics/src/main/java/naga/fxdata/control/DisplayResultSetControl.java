package naga.fxdata.control;

import emul.javafx.beans.property.Property;
import emul.javafx.beans.property.SimpleObjectProperty;
import emul.javafx.scene.control.Control;
import naga.fxdata.displaydata.DisplayResultSet;
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
