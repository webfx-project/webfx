package javafx.scene.control;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.text.Font;
import webfx.kit.mapper.peers.javafxgraphics.markers.HasFontProperty;
import webfx.kit.mapper.peers.javafxgraphics.markers.HasPromptTextProperty;
import webfx.kit.mapper.peers.javafxgraphics.markers.HasTextProperty;

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

    /**
     * Clears the text.
     */
    public void clear() {
        //deselect();
        if (!textProperty.isBound()) {
            setText("");
        }
    }
}
