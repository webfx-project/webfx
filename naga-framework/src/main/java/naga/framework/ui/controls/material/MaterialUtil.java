package naga.framework.ui.controls.material;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import naga.framework.ui.controls.material.skins.MaterialTextFieldSkin;

/**
 * @author Bruno Salmon
 */
public class MaterialUtil {

    public static <T extends TextField> T makeMaterial(T textField) {
        return makeMaterial(textField, new MaterialTextFieldSkin(textField));
    }

    public static <T extends Control> T makeMaterial(T control, Skin<?> materialSkin) {
        if (materialSkin != null)
            control.setSkin(materialSkin);
        control.getStyleClass().add("material");
        return control;
    }

}
