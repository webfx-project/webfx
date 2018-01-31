package naga.framework.ui.controls;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import naga.framework.ui.action.Action;
import naga.framework.ui.controls.material.textfield.MaterialTextField;
import naga.framework.ui.controls.material.textfield.MaterialTextFieldPane;
import naga.framework.ui.controls.material.util.MaterialUtil;
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

    default TextField newTextFieldWithPrompt(Object i18nKey) {
        return translatePromptText(newTextField(), i18nKey);
    }

    default TextField newMaterialTextField() {
        return MaterialUtil.makeMaterial(newTextField());
    }

    default TextField newMaterialTextField(Object labelKey) {
        return newMaterialTextField(labelKey, null);
    }

    default TextField newMaterialTextField(Object labelKey, Object placeholderKey) {
        return setMaterialLabelAndPlaceholder(newMaterialTextField(), labelKey, placeholderKey);
    }

    default PasswordField newPasswordField() {
        return new PasswordField();
    }

    default PasswordField newMaterialPassword() {
        return MaterialUtil.makeMaterial(newPasswordField());
    }

    default PasswordField newMaterialPasswordField(Object labelKey, Object placeholderKey) {
        return setMaterialLabelAndPlaceholder(newMaterialPassword(), labelKey, placeholderKey);
    }

    default <T extends Control> T setMaterialLabelAndPlaceholder(T control, Object labelKey, Object placeholderKey) {
        setMaterialLabelAndPlaceholder(MaterialUtil.getMaterialTextField(control), labelKey, placeholderKey);
        return control;
    }

    default <T extends MaterialTextField> T setMaterialLabelAndPlaceholder(T materialTextField, Object labelKey, Object placeholderKey) {
        translateString(materialTextField.labelTextProperty(), labelKey);
        translateString(materialTextField.placeholderTextProperty(), placeholderKey);
        return materialTextField;
    }

    default MaterialTextFieldPane newMaterialRegion(Region region) {
        return new MaterialTextFieldPane(region);
    }

    default MaterialTextFieldPane newMaterialRegion(Region region, Object labelKey) {
        return newMaterialRegion(region, labelKey, null);
    }

    default MaterialTextFieldPane newMaterialRegion(Region region, Object labelKey, Object placeholderKey) {
        return setMaterialLabelAndPlaceholder(newMaterialRegion(region), labelKey, placeholderKey);
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
