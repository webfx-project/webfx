package javafx.scene.control;

import javafx.util.StringConverter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import webfx.kit.registry.javafxcontrols.JavaFxControlsRegistry;

import java.time.LocalDate;

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
        // From comboBox
        focusedProperty().addListener((ov, t, hasFocus) -> {
            // JDK-8120120 (aka RT-21454) and JDK-8136838
            if (!hasFocus)
                setTextFromTextFieldIntoComboBoxValue();
        });
        valueProperty().addListener((observable, oldValue, newValue) -> resetTextFromValue());
        converterProperty().addListener((observable, oldValue, newValue) -> resetTextFromValue());
    }

    private void resetTextFromValue() {
        if (!resettingValueFromText)
            setText(getConverter().toString(getValue()));
    }

    private boolean resettingValueFromText;

    private void setTextFromTextFieldIntoComboBoxValue() {
        StringConverter<LocalDate> c = getConverter();
        if (c != null) {
            LocalDate oldValue = getValue();
            LocalDate value = oldValue;
            String text = getText();

            // conditional check here added due to RT-28245
            if (oldValue == null && (text == null || text.isEmpty())) {
                value = null;
            } else {
                try {
                    value = c.fromString(text);
                } catch (Exception ex) {
                    // Most likely a parsing error, such as DateTimeParseException
                }
            }

            if ((value != null || oldValue != null) && (value == null || !value.equals(oldValue))) {
                // no point updating values needlessly if they are the same
                resettingValueFromText = true;
                setValue(value);
                resettingValueFromText = false;
            }
        }
    }

    /** from ComboBox (temporary)
     * The value of this ComboBox is defined as the selected item if the input
     * is not editable, or if it is editable, the most recent user action:
     * either the value input by the user, or the last selected item.
     */
    public ObjectProperty<LocalDate> valueProperty() { return value; }
    private ObjectProperty<LocalDate> value = new SimpleObjectProperty<>(this, "value");

    public final void setValue(LocalDate value) { valueProperty().set(value); }
    public final LocalDate getValue() { return valueProperty().get(); }

    public final Property<StringConverter<LocalDate>> converterProperty() { return converter; }
    private final Property<StringConverter<LocalDate>> converter =
            new SimpleObjectProperty<>(this, "converter", null);
    public final void setConverter(StringConverter<LocalDate> value) { converterProperty().setValue(value); }
    public final StringConverter<LocalDate> getConverter() {
        return converterProperty().getValue();
    }

    static {
        JavaFxControlsRegistry.registerDatePicker();
    }
}
