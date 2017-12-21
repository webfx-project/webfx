package mongoose.activities.shared.generic;

import javafx.scene.control.Button;
import mongoose.activities.shared.logic.ui.theme.Theme;
import naga.framework.ui.action.ButtonFactoryMixin;

/**
 * @author Bruno Salmon
 */
public interface MongooseButtonFactoryMixin extends ButtonFactoryMixin {

    @Override
    default Button styleButton(Button button) {
        button.textFillProperty().bind(Theme.mainTextFillProperty());
        return button;
    }

}
