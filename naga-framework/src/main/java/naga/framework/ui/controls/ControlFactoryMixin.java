package naga.framework.ui.controls;

import javafx.scene.control.*;
import javafx.scene.text.Text;
import naga.framework.ui.i18n.I18nMixin;

/**
 * @author Bruno Salmon
 */
public interface ControlFactoryMixin extends I18nMixin {

    default Button newButton() {
        return styleButton(new Button());
    }

    default Button newButton(Object i18nKey) {
        return translateText(newButton(), i18nKey);
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

    default TextField newTextFieldWithPrompt(Object i18nKey) {
        return translatePromptText(new TextField(), i18nKey);
    }

    default TextArea newTextAreaWithPrompt(Object i18nKey) {
        return translatePromptText(new TextArea(), i18nKey);
    }

    default Text newText(Object i18nKey) {
        return translateText(new Text(), i18nKey);
    }

    default Button styleButton(Button button) {
        return button;
    }

}
