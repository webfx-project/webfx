package naga.framework.ui.graphic.controls;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import naga.framework.ui.action.Action;
import naga.framework.ui.graphic.controls.button.ButtonBuilder;
import naga.framework.ui.i18n.I18nMixin;

/**
 * @author Bruno Salmon
 */
public interface ControlFactoryMixin extends I18nMixin {

    default Button newButton() {
        return newButtonBuilder().build();
    }

    default naga.framework.ui.graphic.controls.button.ButtonBuilder newButtonBuilder() {
        return new naga.framework.ui.graphic.controls.button.ButtonBuilder().setI18n(getI18n()).setStyleFunction(this::styleButton);
    }

    default Button newButton(Object i18nKey) {
        return newButtonBuilder(i18nKey).build();
    }

    default naga.framework.ui.graphic.controls.button.ButtonBuilder newButtonBuilder(Object i18nKey) {
        return newButtonBuilder().setI18nKey(i18nKey);
    }

    default Button newButton(Object i18nKey, EventHandler<ActionEvent> onAction) {
        return newButtonBuilder(i18nKey, onAction).build();
    }

    default naga.framework.ui.graphic.controls.button.ButtonBuilder newButtonBuilder(Object i18nKey, EventHandler<ActionEvent> onAction) {
        return newButtonBuilder(i18nKey).setOnAction(onAction);
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
        return translateText(new CheckBox(), i18nKey);
    }

    default RadioButton newRadioButton(Object i18nKey) {
        return translateText(new RadioButton(), i18nKey);
    }

    default RadioButton newRadioButton(Object i18nKey, ToggleGroup toggleGroup) {
        RadioButton radioButton = newRadioButton(i18nKey);
        radioButton.setToggleGroup(toggleGroup);
        return radioButton;
    }

    default Label newLabel(Object i18nKey) {
        return translateText(new Label(), i18nKey);
    }

    default TextField newTextField() {
        return new TextField();
    }

    default TextField newTextFieldWithPrompt(Object i18nKey) {
        return translatePromptText(newTextField(), i18nKey);
    }

    default PasswordField newPasswordField() {
        return new PasswordField();
    }

    default Hyperlink newHyperlink() {
        return new Hyperlink();
    }

    default Hyperlink newHyperlink(Object i18nKey) {
        return translateText(newHyperlink(), i18nKey);
    }

    default Hyperlink newHyperlink(Object i18nKey, EventHandler<ActionEvent> onAction) {
        Hyperlink hyperlink = translateText(newHyperlink(), i18nKey);
        hyperlink.setOnAction(onAction);
        return hyperlink;
    }

    default TextArea newTextAreaWithPrompt(Object i18nKey) {
        return translatePromptText(new TextArea(), i18nKey);
    }

    default Text newText(Object i18nKey) {
        return translateText(new Text(), i18nKey);
    }

}
