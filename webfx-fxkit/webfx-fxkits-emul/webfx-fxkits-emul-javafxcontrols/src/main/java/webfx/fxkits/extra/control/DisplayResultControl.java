package webfx.fxkits.extra.control;

import emul.javafx.beans.property.Property;
import emul.javafx.beans.property.SimpleObjectProperty;
import emul.javafx.scene.control.Control;
import webfx.fxkits.extra.displaydata.DisplayResult;
import webfx.fxkits.extra.displaydata.HasDisplayResultProperty;

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
