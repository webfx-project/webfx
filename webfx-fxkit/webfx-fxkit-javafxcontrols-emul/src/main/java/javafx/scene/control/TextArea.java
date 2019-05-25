package javafx.scene.control;

import webfx.javafxcontrols.registry.JavaFxControlsRegistry;

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

    static {
        JavaFxControlsRegistry.registerTextArea();
    }
}
