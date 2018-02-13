package naga.framework.ui.action;

import javafx.beans.value.ObservableBooleanValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import naga.framework.ui.i18n.I18n;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class ActionRegistry {

    public final static Object OK_ACTION_KEY = "Ok";
    public final static Object CANCEL_ACTION_KEY = "Cancel";
    public final static String SAVE_ACTION_KEY = "Save";
    public final static String REVERT_ACTION_KEY = "Revert";
    public final static String ADD_ACTION_KEY = "Add";
    public final static String REMOVE_ACTION_KEY = "Remove";

    private static final Map<Object, ActionBuilder> actionBuilders = new HashMap<>();
    private static I18n i18n;

    public static void registerAction(ActionBuilder actionBuilder) {
        actionBuilders.put(actionBuilder.getActionKey(), actionBuilder);
    }

    public static void setI18n(I18n i18n) {
        ActionRegistry.i18n = i18n;
    }

    public static Action newAction(Object actionKey, EventHandler<ActionEvent> actionHandler) {
        return newAuthAction(actionKey, actionHandler, null);
    }

    public static ActionBuilder actionBuilder(Object actionKey) {
        ActionBuilder actionBuilder = actionBuilders.get(actionKey);
        if (actionBuilder == null)
            actionBuilder = new ActionBuilder(actionKey).setI18nKey(actionKey).register();
        return actionBuilder;
    }

    public static Action newAuthAction(Object actionKey, EventHandler<ActionEvent> actionHandler, ObservableBooleanValue authorizedProperty) {
        return actionBuilder(actionKey).build(actionHandler, authorizedProperty, i18n);
    }

    // Same API but with Runnable

    public static Action newAction(Object actionKey, Runnable actionHandler) {
        return newAction(actionKey, e -> actionHandler.run());
    }

    public static Action newAuthAction(Object actionKey, Runnable actionHandler, ObservableBooleanValue authorizedProperty) {
        return newAuthAction(actionKey, e -> actionHandler.run(), authorizedProperty);
    }

    //

    public static Action newOkAction(Runnable handler) {
        return newAction(OK_ACTION_KEY, handler);
    }

    public static Action newCancelAction(Runnable handler) {
        return newAction(CANCEL_ACTION_KEY, handler);
    }

    public static Action newSaveAction(Runnable handler) {
        return newAction(SAVE_ACTION_KEY, handler);
    }

    public static Action newRevertAction(Runnable handler) {
        return newAction(REVERT_ACTION_KEY, handler);
    }

    public static Action newAddAction(Runnable handler) {
        return newAction(ADD_ACTION_KEY, handler);
    }

    public static Action newRemoveAction(Runnable handler) {
        return newAction(REMOVE_ACTION_KEY, handler);
    }

}
