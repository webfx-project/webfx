package naga.fxdata.control;

import emul.javafx.beans.property.Property;
import emul.javafx.beans.property.SimpleObjectProperty;
import emul.javafx.scene.control.Control;
import naga.fxdata.displaydata.DisplayResult;
import naga.fxdata.displaydata.HasDisplayResultProperty;

/**
 * @author Bruno Salmon
 */
public abstract class DisplayResultControl extends Control implements
        HasDisplayResultProperty {

    private Property<DisplayResult> displayResultProperty = new SimpleObjectProperty<DisplayResult>() {
        @Override
        protected void invalidated() {
            requestParentLayout();
        }
    };
    @Override
    public Property<DisplayResult> displayResultProperty() {
        return displayResultProperty;
    }

}
