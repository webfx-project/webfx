package naga.toolkit.fx.ext.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.fx.ext.DisplayResultSetControl;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.fx.scene.control.impl.ControlImpl;

/**
 * @author Bruno Salmon
 */
abstract class DisplayResultSetControlImpl extends ControlImpl implements DisplayResultSetControl {

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
