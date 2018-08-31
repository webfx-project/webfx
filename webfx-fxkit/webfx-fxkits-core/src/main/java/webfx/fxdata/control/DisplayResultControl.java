package webfx.fxdata.control;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import webfx.fxdata.displaydata.DisplayResult;
import javafx.scene.control.Control;
import webfx.fxdata.displaydata.HasDisplayResultProperty;

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
