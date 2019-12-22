package webfx.extras.materialdesign.textfield;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Paint;

/**
 * @author Bruno Salmon
 */
public interface MaterialTextFieldMixin extends MaterialTextField {

    MaterialTextField getMaterialTextField();

    @Override
    default ObservableValue inputProperty() {
        return getMaterialTextField().inputProperty();
    }

    @Override
    default boolean isInputEmpty() {
        return getMaterialTextField().isInputEmpty();
    }

    default StringProperty placeholderTextProperty() {
        return getMaterialTextField().placeholderTextProperty();
    }

    default String getPlaceholderText() {
        return getMaterialTextField().getPlaceholderText();
    }

    default void setPlaceholderText(String text) {
        getMaterialTextField().setPlaceholderText(text);
    }

    default StringProperty labelTextProperty() {
        return getMaterialTextField().labelTextProperty();
    }

    default String getLabelText() {
        return getMaterialTextField().getLabelText();
    }

    default void setLabelText(String text) {
        getMaterialTextField().setLabelText(text);
    }

    default StringProperty helperTextProperty() {
        return getMaterialTextField().helperTextProperty();
    }

    default String getHelperText() {
        return getMaterialTextField().getHelperText();
    }

    default void setHelperText(String text) {
        getMaterialTextField().setHelperText(text);
    }

    default StringProperty errorMessageProperty() {
        return getMaterialTextField().errorMessageProperty();
    }

    default String getErrorMessage() {
        return getMaterialTextField().getErrorMessage();
    }

    default void setErrorMessage(String text) {
        getMaterialTextField().setErrorMessage(text);
    }

    default ReadOnlyBooleanProperty disabledProperty() {
        return getMaterialTextField().disabledProperty();
    }

    default boolean isDisabled() {
        return getMaterialTextField().isDisabled();
    }

    default void setDisable(boolean disable) {
        getMaterialTextField().setDisable(disable);
    }

    default BooleanProperty requiredProperty() {
        return getMaterialTextField().requiredProperty();
    }

    default boolean isRequired() {
        return getMaterialTextField().isRequired();
    }

    default void setRequired(boolean required) {
        getMaterialTextField().setRequired(required);
    }

    default ReadOnlyBooleanProperty focusedProperty() {
        return getMaterialTextField().focusedProperty();
    }

    default boolean isFocused() {
        return getMaterialTextField().isFocused();
    }

    default BooleanProperty denseSpacingProperty() {
        return getMaterialTextField().denseSpacingProperty();
    }

    default boolean isDenseSpacing() {
        return getMaterialTextField().isDenseSpacing();
    }

    default void setDenseSpacing(boolean denseSpacing) {
        getMaterialTextField().setDenseSpacing(denseSpacing);
    }

    default Property<Paint> disabledFillProperty() {
        return getMaterialTextField().disabledFillProperty();
    }

    @Override
    default Paint getDisabledFill() {
        return getMaterialTextField().getDisabledFill();
    }

    @Override
    default void setDisabledFill(Paint disabledFill) {
        getMaterialTextField().setDisabledFill(disabledFill);
    }

    default Property<Paint> inputTextFillProperty() {
        return getMaterialTextField().inputTextFillProperty();
    }

    @Override
    default Paint getInputTextFill() {
        return getMaterialTextField().getInputTextFill();
    }

    @Override
    default void setInputTextFill(Paint inputTextFill) {
        getMaterialTextField().setInputTextFill(inputTextFill);
    }

    default Property<Paint> invalidTextFillProperty() {
        return getMaterialTextField().invalidTextFillProperty();
    }

    @Override
    default Paint getInvalidTextFill() {
        return getMaterialTextField().getInvalidTextFill();
    }

    @Override
    default void setInvalidTextFill(Paint invalidTextFill) {
        getMaterialTextField().setInputTextFill(invalidTextFill);
    }

    default Property<Paint> invalidLineFillProperty() {
        return getMaterialTextField().invalidLineFillProperty();
    }

    @Override
    default Paint getInvalidLineFill() {
        return getMaterialTextField().getInvalidLineFill();
    }

    @Override
    default void setInvalidLineFill(Paint invalidLineFill) {
        getMaterialTextField().setInvalidLineFill(invalidLineFill);
    }

    default Property<Paint> focusLabelFillProperty() {
        return getMaterialTextField().focusLabelFillProperty();
    }

    @Override
    default Paint getFocusLabelFill() {
        return getMaterialTextField().getFocusLabelFill();
    }

    @Override
    default void setFocusLabelFill(Paint focusLabelFill) {
        getMaterialTextField().setFocusLabelFill(focusLabelFill);
    }

    default Property<Paint> focusLineFillProperty() {
        return getMaterialTextField().focusLineFillProperty();
    }

    @Override
    default Paint getFocusLineFill() {
        return getMaterialTextField().getFocusLineFill();
    }

    @Override
    default void setFocusLineFill(Paint focusLineFill) {
        getMaterialTextField().setFocusLineFill(focusLineFill);
    }

    default Property<Paint> idleTextFillProperty() {
        return getMaterialTextField().idleTextFillProperty();
    }

    @Override
    default Paint getIdleTextFill() {
        return getMaterialTextField().getIdleTextFill();
    }

    @Override
    default void setIdleTextFill(Paint idleTextFill) {
        getMaterialTextField().setIdleTextFill(idleTextFill);
    }
}
