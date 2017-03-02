package naga.framework.ui.controls;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import naga.framework.ui.action.Action;
import naga.framework.ui.i18n.I18n;

/**
 * @author Bruno Salmon
 */
public class ButtonUtil {

    public static Button newButton(Action action, I18n i18n) {
        return newButton(action.getIconUrlOrJson(), action.getI18nKey(), i18n, action.getHandler());
    }

    public static Button newButton(Object iconUrlOrJson, Object translationKey, I18n i18n, EventHandler<ActionEvent> onAction) {
        return newButton(ImageViewUtil.createImageView(iconUrlOrJson), translationKey, i18n, onAction);
    }

    public static Button newButton(Node graphic, Object translationKey, I18n i18n, EventHandler<ActionEvent> onAction) {
        Button button = i18n.translateText(new Button(null, graphic), translationKey);
        button.setOnAction(onAction);
        return button;
    }
}
