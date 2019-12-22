package webfx.extras.materialdesign.textfield;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Paint;

/**
 * @author Bruno Salmon
 */
public interface MaterialTextField {

    ObservableValue inputProperty();

    default boolean isInputEmpty() {
        ObservableValue observableValue = inputProperty();
        if (inputProperty() == null)
            return false;
        Object input = observableValue.getValue();
        return input == null || input instanceof String && ((String) input).isEmpty();
    }

    StringProperty placeholderTextProperty();

    default String getPlaceholderText() {
        return placeholderTextProperty().getValue();
    }

    default void setPlaceholderText(String text) {
        placeholderTextProperty().setValue(text);
    }

    StringProperty labelTextProperty();

    default String getLabelText() {
        return labelTextProperty().getValue();
    }

    default void setLabelText(String text) {
        labelTextProperty().setValue(text);
    }

    StringProperty helperTextProperty();

    default String getHelperText() {
        return helperTextProperty().getValue();
    }

    default void setHelperText(String text) {
        helperTextProperty().setValue(text);
    }

    StringProperty errorMessageProperty();

    default String getErrorMessage() {
        return errorMessageProperty().getValue();
    }

    default void setErrorMessage(String text) {
        errorMessageProperty().setValue(text);
    }

    ReadOnlyBooleanProperty disabledProperty();

    default boolean isDisabled() {
        return disabledProperty().getValue();
    }

    default void setDisable(boolean disabled) {
        ((BooleanProperty) disabledProperty()).set(disabled);
    }

    BooleanProperty requiredProperty();

    default boolean isRequired() {
        return requiredProperty().getValue();
    }

    default void setRequired(boolean required) {
        requiredProperty().set(required);
    }

    ReadOnlyBooleanProperty focusedProperty();

    default boolean isFocused() {
        return focusedProperty().getValue();
    }

    BooleanProperty denseSpacingProperty();

    default boolean isDenseSpacing() {
        return denseSpacingProperty().getValue();
    }

    default void setDenseSpacing(boolean denseSpacing) {
        denseSpacingProperty().set(denseSpacing);
    }

    Property<Paint> disabledFillProperty(); // default = gray 8a8a8a - for disabled input text, label, placeholder (always), and idle line

    default Paint getDisabledFill() {
        return disabledFillProperty().getValue();
    }

    default void setDisabledFill(Paint disabledFill) {
        disabledFillProperty().setValue(disabledFill);
    }

    Property<Paint> inputTextFillProperty(); // default = black 1e1e1e (when not disabled - editing or not)

    default Paint getInputTextFill() {
        return inputTextFillProperty().getValue();
    }

    default void setInputTextFill(Paint inputTextFill) {
        inputTextFillProperty().setValue(inputTextFill);
    }

    Property<Paint> invalidTextFillProperty(); // default = red fc3259 - also for error message

    default Paint getInvalidTextFill() {
        return invalidTextFillProperty().getValue();
    }

    default void setInvalidTextFill(Paint invalidTextFill) {
        invalidTextFillProperty().setValue(invalidTextFill);
    }

    Property<Paint> invalidLineFillProperty(); // default = red ff1744 - also for cursor ?

    default Paint getInvalidLineFill() {
        return invalidLineFillProperty().getValue();
    }

    default void setInvalidLineFill(Paint invalidLineFill) {
        invalidLineFillProperty().setValue(invalidLineFill);
    }

    Property<Paint> focusLabelFillProperty(); // default = blue 4273fc

    default Paint getFocusLabelFill() {
        return focusLabelFillProperty().getValue();
    }

    default void setFocusLabelFill(Paint focusLabelFill) {
        focusLabelFillProperty().setValue(focusLabelFill);
    }

    Property<Paint> focusLineFillProperty(); // default = blue 2962ff - also for cursor

    default Paint getFocusLineFill() {
        return focusLineFillProperty().getValue();
    }

    default void setFocusLineFill(Paint focusLineFill) {
        focusLineFillProperty().setValue(focusLineFill);
    }

    Property<Paint> idleTextFillProperty(); // default = gray 6d6d6d - also for helper text (always)

    default Paint getIdleTextFill() {
        return idleTextFillProperty().getValue();
    }

    default void setIdleTextFill(Paint idleTextFill) {
        idleTextFillProperty().setValue(idleTextFill);
    }

}
