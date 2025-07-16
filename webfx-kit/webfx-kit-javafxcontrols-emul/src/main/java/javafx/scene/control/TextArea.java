package javafx.scene.control;

import dev.webfx.kit.registry.javafxcontrols.JavaFxControlsRegistry;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import javafx.scene.input.MouseEvent;

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


    {
        // In WebFX, TextArea doesn't have a skin because it is mapped directly into an HTML TextArea. But we mimic at
        // least the standard JavaFX behavior that it consumes all mouse events.
        addEventHandler(MouseEvent.ANY, Event::consume); // Same as SkinBase.consumeMouseEvents(true)
    }

    static {
        JavaFxControlsRegistry.registerTextArea();
    }
}
