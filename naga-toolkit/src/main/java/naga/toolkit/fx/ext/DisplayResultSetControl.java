package naga.toolkit.fx.ext;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.fx.scene.control.Control;
import naga.toolkit.display.HasDisplayResultSetProperty;

/**
 * @author Bruno Salmon
 */
public abstract class DisplayResultSetControl extends Control implements HasDisplayResultSetProperty {

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
