package webfx.framework.ui.controls;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import webfx.framework.services.i18n.I18n;
import webfx.framework.ui.action.Action;
import webfx.framework.ui.action.ActionBuilder;
import webfx.framework.ui.action.ActionFactoryMixin;
import webfx.framework.ui.controls.button.ButtonBuilder;

/**
 * @author Bruno Salmon
 */
public interface ControlFactoryMixin extends ActionFactoryMixin {

    default Button newButton() {
        return newButtonBuilder().build();
    }

    default ButtonBuilder newButtonBuilder() {
        return new ButtonBuilder().setStyleFunction(this::styleButton);
    }

    default Button newButton(Object i18nKey) {
        return newButtonBuilder(i18nKey).build();
    }

    default ButtonBuilder newButtonBuilder(Object i18nKey) {
        return newButtonBuilder().setI18nKey(i18nKey);
    }

    default Button newButton(Object i18nKey, EventHandler<ActionEvent> actionHandler) {
        return newButtonBuilder(i18nKey, actionHandler).build();
    }

    default ButtonBuilder newButtonBuilder(Object i18nKey, EventHandler<ActionEvent> actionHandler) {
        return newButtonBuilder(i18nKey).setOnAction(actionHandler);
    }

    default Button newButton(ActionBuilder actionBuilder) {
        return newButtonBuilder(actionBuilder.build()).build();
    }

    default Button newButton(Action action) {
        return newButtonBuilder(action).build();
    }

    default ButtonBuilder newButtonBuilder(Action action) {
        return newButtonBuilder().setAction(action);
    }

    default Button styleButton(Button button) {
        return button;
    }

    default CheckBox newCheckBox(Object i18nKey) {
        return I18n.translateText(new CheckBox(), i18nKey);
    }

    default RadioButton newRadioButton(Object i18nKey) {
        return I18n.translateText(new RadioButton(), i18nKey);
    }

    default RadioButton newRadioButton(Object i18nKey, ToggleGroup toggleGroup) {
        RadioButton radioButton = newRadioButton(i18nKey);
        radioButton.setToggleGroup(toggleGroup);
        return radioButton;
    }

    default Label newLabel(Object i18nKey) {
        return I18n.translateText(new Label(), i18nKey);
    }

    default TextField newTextField() {
        return new TextField();
    }

    default TextField newTextFieldWithPrompt(Object i18nKey) {
        return I18n.translatePromptText(newTextField(), i18nKey);
    }

    default PasswordField newPasswordField() {
        return new PasswordField();
    }

    default Hyperlink newHyperlink() {
        return new Hyperlink();
    }

    default Hyperlink newHyperlink(Object i18nKey) {
        return I18n.translateText(newHyperlink(), i18nKey);
    }

    default Hyperlink newHyperlink(Object i18nKey, EventHandler<ActionEvent> onAction) {
        Hyperlink hyperlink = I18n.translateText(newHyperlink(), i18nKey);
        hyperlink.setOnAction(onAction);
        return hyperlink;
    }

    default TextArea newTextAreaWithPrompt(Object i18nKey) {
        return I18n.translatePromptText(new TextArea(), i18nKey);
    }

    default Text newText(Object i18nKey) {
        return I18n.translateText(new Text(), i18nKey);
    }

}
