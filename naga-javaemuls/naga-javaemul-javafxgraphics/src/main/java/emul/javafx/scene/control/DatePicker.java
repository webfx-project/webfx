package emul.javafx.scene.control;

/**
 * @author Bruno Salmon
 */
public class DatePicker extends TextField {

    /**
     * Creates a {@code TextField} with empty text content.
     */
    public DatePicker() {
        this("");
    }

    /**
     * Creates a {@code TextField} with initial text content.
     *
     * @param text A string for text content.
     */
    public DatePicker(String text) {
        //super(new TextFieldContent());
        getStyleClass().add("text-field");
        //setAccessibleRole(AccessibleRole.TEXT_FIELD);
        setText(text);
    }
}
