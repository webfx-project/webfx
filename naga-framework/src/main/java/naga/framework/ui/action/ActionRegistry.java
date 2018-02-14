package naga.framework.ui.action;

import javafx.beans.value.ObservableBooleanValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import naga.framework.ui.action.impl.ActionRegistryImpl;
import naga.framework.ui.i18n.I18n;

/**
 * @author Bruno Salmon
 */
public interface ActionRegistry {

    Object OK_ACTION_KEY = "Ok";
    Object CANCEL_ACTION_KEY = "Cancel";
    Object SAVE_ACTION_KEY = "Save";
    Object REVERT_ACTION_KEY = "Revert";
    Object ADD_ACTION_KEY = "Add";
    Object REMOVE_ACTION_KEY = "Remove";

    void registerAction(ActionBuilder actionBuilder);

    ActionBuilder newActionBuilder(Object actionKey);

    void setI18n(I18n i18n);

    I18n getI8n();

    default Action newAction(Object actionKey, EventHandler<ActionEvent> actionHandler) {
        return newAuthAction(actionKey, actionHandler, null);
    }

    default Action newAuthAction(Object actionKey, EventHandler<ActionEvent> actionHandler, ObservableBooleanValue authorizedProperty) {
        return newActionBuilder(actionKey).setActionHandler(actionHandler).setAuthorizedProperty(authorizedProperty).setI18n(getI8n()).build();
    }

    // Same API but with Runnable

    default Action newAction(Object actionKey, Runnable actionHandler) {
        return newAction(actionKey, e -> actionHandler.run());
    }

    default Action newAuthAction(Object actionKey, Runnable actionHandler, ObservableBooleanValue authorizedProperty) {
        return newAuthAction(actionKey, e -> actionHandler.run(), authorizedProperty);
    }

    //

    default Action newOkAction(Runnable handler) {
        return newAction(OK_ACTION_KEY, handler);
    }

    default Action newCancelAction(Runnable handler) {
        return newAction(CANCEL_ACTION_KEY, handler);
    }

    default Action newSaveAction(Runnable handler) {
        return newAction(SAVE_ACTION_KEY, handler);
    }

    default Action newRevertAction(Runnable handler) {
        return newAction(REVERT_ACTION_KEY, handler);
    }

    default Action newAddAction(Runnable handler) {
        return newAction(ADD_ACTION_KEY, handler);
    }

    default Action newRemoveAction(Runnable handler) {
        return newAction(REMOVE_ACTION_KEY, handler);
    }

    static ActionRegistry get() {
        return ActionRegistryImpl.get();
    }
}
