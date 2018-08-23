package emul.javafx.scene.control;

import emul.javafx.beans.property.Property;
import emul.javafx.beans.property.SimpleObjectProperty;
import emul.javafx.beans.property.SimpleStringProperty;
import emul.javafx.beans.property.StringProperty;
import emul.javafx.scene.text.Font;
import naga.fx.properties.markers.HasFontProperty;
import naga.fx.properties.markers.HasPromptTextProperty;
import naga.fx.properties.markers.HasTextProperty;

/**
 * @author Bruno Salmon
 */
public abstract class TextInputControl extends Control implements
        HasFontProperty,
        HasTextProperty,
        HasPromptTextProperty {

    private final Property<Font> fontProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Font> fontProperty() {
        return fontProperty;
    }

    private final StringProperty textProperty = new SimpleStringProperty();
    @Override
    public StringProperty textProperty() {
        return textProperty;
    }

    private final StringProperty promptProperty = new SimpleStringProperty();
    @Override
    public StringProperty promptTextProperty() {
        return promptProperty;
    }

    /**
     * Indicates whether this TextInputControl can be edited by the user.
     */
    private Property<Boolean> editable = new SimpleObjectProperty<>(this, "editable", true);
    public final boolean isEditable() { return editable.getValue(); }
    public final void setEditable(boolean value) { editable.setValue(value); }
    public final Property<Boolean> editableProperty() { return editable; }

}
