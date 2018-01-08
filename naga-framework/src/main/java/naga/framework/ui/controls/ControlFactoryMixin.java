package naga.framework.ui.controls;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import naga.framework.ui.action.Action;
import naga.framework.ui.controls.skins.MaterialTextFieldSkin;
import naga.framework.ui.i18n.I18nMixin;

/**
 * @author Bruno Salmon
 */
public interface ControlFactoryMixin extends I18nMixin {

    default Button newButton() {
        return newButtonBuilder().build();
    }

    default ButtonBuilder newButtonBuilder() {
        return new ButtonBuilder().setI18n(getI18n()).setStyleFunction(this::styleButton);
    }

    default Button newButton(Object i18nKey) {
        return newButtonBuilder(i18nKey).build();
    }

    default ButtonBuilder newButtonBuilder(Object i18nKey) {
        return newButtonBuilder().setI18nKey(i18nKey);
    }

    default Button newButton(Object i18nKey, EventHandler<ActionEvent> onAction) {
        return newButtonBuilder(i18nKey, onAction).build();
    }

    default ButtonBuilder newButtonBuilder(Object i18nKey, EventHandler<ActionEvent> onAction) {
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

    default TextField newMaterialTextField() {
        return makeMaterial(newTextField());
    }

    default TextField newTextFieldWithPrompt(Object i18nKey) {
        return translatePromptText(newTextField(), i18nKey);
    }

    default TextField newMaterialTextFieldWithPrompt(Object i18nKey) {
        return makeMaterial(newTextFieldWithPrompt(i18nKey));
    }

    default PasswordField newPasswordField() {
        return new PasswordField();
    }

    default PasswordField newMaterialPasswordField() {
        return makeMaterial(newPasswordField());
    }

    default Hyperlink newHyperlink() {
        return new Hyperlink();
    }

    static <T extends TextField> T makeMaterial(T textField) {
        return makeMaterial(textField, new MaterialTextFieldSkin(textField));
    }

    static <T extends Control> T makeMaterial(T control, Skin<?> materialSkin) {
        if (materialSkin != null)
            control.setSkin(materialSkin);
        return addStyleClass(control, "material");
    }

    static <T extends Node> T addStyleClass(T node, String styleClass) {
        node.getStyleClass().add(styleClass);
        return node;
    }

    default TextArea newTextAreaWithPrompt(Object i18nKey) {
        return translatePromptText(new TextArea(), i18nKey);
    }

    default Text newText(Object i18nKey) {
        return translateText(new Text(), i18nKey);
    }

}
