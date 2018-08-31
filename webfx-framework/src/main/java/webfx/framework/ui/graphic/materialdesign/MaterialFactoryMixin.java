package webfx.framework.ui.graphic.materialdesign;

import javafx.scene.control.Control;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import webfx.framework.services.i18n.I18n;
import webfx.framework.ui.graphic.controls.ControlFactoryMixin;
import webfx.framework.ui.graphic.materialdesign.textfield.MaterialTextField;
import webfx.framework.ui.graphic.materialdesign.textfield.MaterialTextFieldPane;
import webfx.framework.ui.graphic.materialdesign.util.MaterialUtil;

/**
 * @author Bruno Salmon
 */
public interface MaterialFactoryMixin extends ControlFactoryMixin {

    default TextField newMaterialTextField() {
        return MaterialUtil.makeMaterial(newTextField());
    }

    default TextField newMaterialTextField(Object labelKey) {
        return newMaterialTextField(labelKey, null);
    }

    default TextField newMaterialTextField(Object labelKey, Object placeholderKey) {
        return setMaterialLabelAndPlaceholder(newMaterialTextField(), labelKey, placeholderKey);
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
        I18n.translateString(materialTextField.labelTextProperty(), labelKey);
        I18n.translateString(materialTextField.placeholderTextProperty(), placeholderKey);
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
}
