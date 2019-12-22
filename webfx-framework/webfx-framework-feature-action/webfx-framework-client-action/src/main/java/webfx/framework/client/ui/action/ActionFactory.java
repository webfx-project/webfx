package webfx.framework.client.ui.action;

import javafx.beans.value.ObservableBooleanValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * @author Bruno Salmon
 */
public interface ActionFactory extends StandardActionKeys {

    ActionBuilder newActionBuilder(Object actionKey);

    default Action newAction(Object actionKey) {
        return newAction(actionKey, (EventHandler<ActionEvent>) null);
    }

    default Action newAction(Object actionKey, Object graphicUrlOrJson) {
        return newAction(actionKey, graphicUrlOrJson, (EventHandler<ActionEvent>) null);
    }

    default Action newAction(Object actionKey, EventHandler<ActionEvent> actionHandler) {
        return newAuthAction(actionKey, actionHandler, null);
    }

    default Action newAction(Object actionKey, Object graphicUrlOrJson, EventHandler<ActionEvent> actionHandler) {
        return newAuthAction(actionKey, graphicUrlOrJson, actionHandler, null);
    }

    default Action newAuthAction(Object actionKey, ObservableBooleanValue authorizedProperty) {
        return newAuthAction(actionKey, (EventHandler<ActionEvent>) null, authorizedProperty);
    }

    default Action newAuthAction(Object actionKey, Object graphicUrlOrJson, ObservableBooleanValue authorizedProperty) {
        return newAuthAction(actionKey, graphicUrlOrJson, null, authorizedProperty);
    }

    default Action newAuthAction(Object actionKey, EventHandler<ActionEvent> actionHandler, ObservableBooleanValue authorizedProperty) {
        return newActionBuilder(actionKey).setActionHandler(actionHandler).setAuthorizedProperty(authorizedProperty).build();
    }

    default Action newAuthAction(Object actionKey, Object graphicUrlOrJson, EventHandler<ActionEvent> actionHandler, ObservableBooleanValue authorizedProperty) {
        return newActionBuilder(actionKey).setGraphicUrlOrJson(graphicUrlOrJson).setActionHandler(actionHandler).setAuthorizedProperty(authorizedProperty).build();
    }

    // Same API but with Runnable

    default Action newAction(Object actionKey, Runnable actionHandler) {
        return newAction(actionKey, e -> actionHandler.run());
    }

    default Action newAction(Object actionKey, Object graphicUrlOrJson, Runnable actionHandler) {
        return newAction(actionKey, graphicUrlOrJson, e -> actionHandler.run());
    }

    default Action newAuthAction(Object actionKey, Runnable actionHandler, ObservableBooleanValue authorizedProperty) {
        return newAuthAction(actionKey, e -> actionHandler.run(), authorizedProperty);
    }

    // Standard actions factories

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

}
