package naga.framework.ui.action;

import javafx.scene.control.Button;
import naga.framework.ui.controls.ControlFactoryMixin;

/**
 * @author Bruno Salmon
 */
public interface ButtonFactoryMixin extends ControlFactoryMixin {

    default Button newButton(Object i18nKey, Runnable handler) {
        return newButton(ActionRegistry.newAction(i18nKey, handler));
    }

    default Button newButton(Action action) {
        return styleButton(action.toButton(getI18n()));
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
