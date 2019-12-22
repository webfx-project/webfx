package webfx.framework.client.ui.controls;

import javafx.scene.control.Control;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import webfx.framework.client.services.i18n.I18n;
import webfx.extras.materialdesign.textfield.MaterialTextField;
import webfx.extras.materialdesign.textfield.MaterialTextFieldPane;
import webfx.extras.materialdesign.util.MaterialUtil;

/**
 * @author Bruno Salmon
 */
public interface MaterialFactoryMixin extends ControlFactoryMixin {

    default TextField newMaterialTextField() {
        return MaterialUtil.makeMaterial(newTextField());
    }

    default TextField newMaterialTextField(Object i18nKey) {
        return setMaterialLabelAndPlaceholder(newMaterialTextField(), i18nKey);
    }

    default PasswordField newMaterialPassword() {
        return MaterialUtil.makeMaterial(newPasswordField());
    }

    default PasswordField newMaterialPasswordField(Object i18nKey) {
        return setMaterialLabelAndPlaceholder(newMaterialPassword(), i18nKey);
    }

    default <T extends Control> T setMaterialLabelAndPlaceholder(T control, Object i18nKey) {
        setMaterialLabelAndPlaceholder(MaterialUtil.getMaterialTextField(control), i18nKey);
        return control;
    }

    default <T extends MaterialTextField> T setMaterialLabelAndPlaceholder(T materialTextField, Object i18nKey) {
        I18n.bindI18nTextProperty(materialTextField.labelTextProperty(), i18nKey); // Linking the material labelText property with the i18n text property
        I18n.bindI18nPromptProperty(materialTextField.placeholderTextProperty(), i18nKey); // Linking the material placeholder property with the i18n proppt property
        return materialTextField;
    }

    default MaterialTextFieldPane newMaterialRegion(Region region) {
        return new MaterialTextFieldPane(region);
    }

    default MaterialTextFieldPane newMaterialRegion(Region region, Object i18nKey) {
        return setMaterialLabelAndPlaceholder(newMaterialRegion(region), i18nKey);
    }
}
