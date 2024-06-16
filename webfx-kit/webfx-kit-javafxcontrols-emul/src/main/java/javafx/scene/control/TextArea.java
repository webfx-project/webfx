package javafx.scene.control;

import dev.webfx.kit.registry.javafxcontrols.JavaFxControlsRegistry;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * @author Bruno Salmon
 */
public class TextArea extends TextInputControl {

    /**
     * Creates a {@code TextField} with empty text content.
     */
    public TextArea() {
        this("");
    }

    /**
     * Creates a {@code TextField} with initial text content.
     *
     * @param text A string for text content.
     */
    public TextArea(String text) {
        //super(new TextFieldContent());
        getStyleClass().add("text-field");
        //setAccessibleRole(AccessibleRole.TEXT_FIELD);
        setText(text);
    }

    private final BooleanProperty wrapTextProperty = new SimpleBooleanProperty(false);

    public final BooleanProperty wrapTextProperty() { return wrapTextProperty; }
    public final boolean isWrapText() { return wrapTextProperty.getValue(); }
    public final void setWrapText(boolean value) { wrapTextProperty.setValue(value); }


    static {
        JavaFxControlsRegistry.registerTextArea();
    }
}
