package javafx.scene.control;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Tooltip is not yet supported in WebFX. This class is just here as a placeholder to prevent compilation errors with
 * JavaFX code using it in a minimal way (just text property for now). Asked by a WebFX user.
 *
 * @author Bruno Salmon
 */
public class Tooltip extends PopupControl {

    public Tooltip() {
        this(null);
    }

    public Tooltip(String text) {
        super();
        setText(text);
    }

    public final StringProperty textProperty() { return text; }
    public final void setText(String value) {
        textProperty().setValue(value);
    }
    public final String getText() { return text.getValue() == null ? "" : text.getValue(); }
    private final StringProperty text = new SimpleStringProperty(this, "text", "");

}
