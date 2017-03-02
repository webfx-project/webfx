package naga.framework.ui.action;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

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

    private final static Map<Object, Action> actions = new HashMap<>();

    public static void registerAction(Object actionKey, Action action) {
        actions.put(actionKey, action);
    }

    public static Action getAction(Object actionKey) {
        return actions.get(actionKey);
    }

    public static Action getAction(Object actionKey, EventHandler<ActionEvent> handler) {
        Action action = getAction(actionKey);
        if (action != null)
            return Action.create(action.getI18nKey(), action.getIconUrlOrJson(), handler);
        return Action.create(actionKey, null, handler);
    }

    public static Action newAction(Object actionKey, Runnable handler) {
        return getAction(actionKey, e -> handler.run());
    }

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
