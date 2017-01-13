package naga.fx.scene.control;

/**
 * @author Bruno Salmon
 */
public class TextField extends TextInputControl {

    /**
     * Creates a {@code TextField} with empty text content.
     */
    public TextField() {
        this("");
    }

    /**
     * Creates a {@code TextField} with initial text content.
     *
     * @param text A string for text content.
     */
    public TextField(String text) {
        //super(new TextFieldContent());
        getStyleClass().add("text-field");
        //setAccessibleRole(AccessibleRole.TEXT_FIELD);
        setText(text);
    }

}
