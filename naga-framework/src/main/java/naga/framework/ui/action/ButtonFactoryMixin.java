package naga.framework.ui.action;

import javafx.scene.control.Button;
import naga.framework.ui.i18n.I18n;

/**
 * @author Bruno Salmon
 */
public interface ButtonFactoryMixin {

    I18n getI18n();

    default Button newButton(Object translationKey, Runnable handler) {
        return newButton(ActionRegistry.newAction(translationKey, handler));
    }

    default Button newButton(Action action) {
        return action.toButton(getI18n());
    }

    default Button newOkButton(Runnable handler) {
        return newButton(ActionRegistry.newOkAction(handler));
    }

    default Button newCancelButton(Runnable handler) {
        return newButton(ActionRegistry.newCancelAction(handler));
    }

    default Button newRemoveButton(Runnable handler) {
        return newButton(ActionRegistry.newRemoveAction(handler));
    }

}
