package webfx.fxkits.extra.control;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import webfx.fxkits.extra.displaydata.DisplayResult;
import javafx.scene.control.Control;
import webfx.fxkits.extra.displaydata.HasDisplayResultProperty;

/**
 * @author Bruno Salmon
 */
public abstract class DisplayResultControl extends Control implements
        HasDisplayResultProperty {

    private final Property<DisplayResult> displayResultProperty = new SimpleObjectProperty<DisplayResult>() {
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
