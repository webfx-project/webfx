package naga.framework.ui.graphic.design.material.util;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import naga.framework.ui.graphic.design.material.textfield.MaterialTextField;
import naga.framework.ui.graphic.design.material.textfield.MaterialTextFieldSkin;

/**
 * @author Bruno Salmon
 */
public class MaterialUtil {

    public static <T extends TextField> T makeMaterial(T textField) {
        return makeMaterial(textField, new MaterialTextFieldSkin(textField));
    }

/*
    public static <T extends Button> T makeMaterial(T button) {
        return makeMaterial(button, new MaterialButtonSkin(button));
    }

*/
    public static <T extends Control> T makeMaterial(T control, Skin<?> materialSkin) {
        if (materialSkin != null)
            control.setSkin(materialSkin);
        control.getStyleClass().add("material");
        return control;
    }

    public static MaterialTextField getMaterialTextField(Control control) {
        return getMaterialTextField(control.getSkin());
    }

    public static MaterialTextField getMaterialTextField(Skin<?> skin) {
        if (skin instanceof MaterialTextField)
            return ((MaterialTextField) skin);
        return null;
    }

}
