package mongooses.core.activities.sharedends.generic;

import javafx.scene.control.Button;
import mongooses.core.activities.sharedends.logic.ui.theme.Theme;
import webfx.framework.ui.controls.button.ButtonFactoryMixin;
import webfx.framework.ui.controls.button.ButtonBuilder;
import webfx.framework.ui.controls.MaterialFactoryMixin;

/**
 * @author Bruno Salmon
 */
public interface MongooseButtonFactoryMixin extends ButtonFactoryMixin, MaterialFactoryMixin {

    @Override
    default Button styleButton(Button button) {
        button.textFillProperty().bind(Theme.mainTextFillProperty());
        return button;
    }

    default Button newBookButton() {
        return newBookButtonBuilder().build();
    }

    default ButtonBuilder newBookButtonBuilder() {
        return newColorButtonBuilder("Book>>", "#7fd504", "#2a8236");
    }

    default Button newSoldoutButton() {
        return newSoldoutButtonBuilder().build();
    }

    default ButtonBuilder newSoldoutButtonBuilder() {
        return newColorButtonBuilder("Soldout", "#e92c04", "#853416");
    }

}
