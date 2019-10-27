package webfx.extras.visual.controls;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import webfx.extras.visual.VisualResult;
import webfx.extras.visual.HasVisualResultProperty;
import javafx.scene.control.Control;

/**
 * @author Bruno Salmon
 */
public abstract class VisualResultControl extends Control implements
        HasVisualResultProperty {

    private final Property<VisualResult> visualResultProperty = new SimpleObjectProperty<VisualResult>() {
        @Override
        protected void invalidated() {
            requestParentLayout();
        }
    };
    @Override
    public Property<VisualResult> visualResultProperty() {
        return visualResultProperty;
    }
}
