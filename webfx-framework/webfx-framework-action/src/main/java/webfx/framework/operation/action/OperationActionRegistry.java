package webfx.framework.operation.action;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import webfx.framework.operation.HasOperationCode;
import webfx.framework.services.authz.mixin.AuthorizationUtil;
import webfx.framework.ui.action.Action;
import webfx.framework.ui.action.ActionBinder;
import webfx.platforms.core.services.uischeduler.UiScheduler;
import webfx.platforms.core.util.async.AsyncFunction;
import webfx.platforms.core.util.async.Future;
import java.util.function.Function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class OperationActionRegistry {

    private static OperationActionRegistry INSTANCE;

    private final Map<Object /* key = request class or operation code */, Action> operationActions = new HashMap<>();
    private Collection<OperationAction> notYetBoundOperationActions;
    private Map<Object, ObjectProperty<OperationAction>> operationActionProperties;
    private Runnable pendingBindRunnable;

    public static OperationActionRegistry getInstance() {
        if (INSTANCE == null)
            INSTANCE = new OperationActionRegistry();
        return INSTANCE;
    }

    public <A> OperationActionRegistry registerOperationAction(Class<A> operationRequestClass, Action operationAction) {
        operationActions.put(operationRequestClass, operationAction);
        return checkPendingOperationBindings();
    }

    public OperationActionRegistry registerOperationAction(Object operationCode, Action operationAction) {
        operationActions.put(operationCode, operationAction);
        return checkPendingOperationBindings();
    }

    private OperationActionRegistry checkPendingOperationBindings() {
        if (notYetBoundOperationActions != null && pendingBindRunnable == null) {
            UiScheduler.scheduleDeferred(pendingBindRunnable = () -> {
                Collection<OperationAction> actions =  notYetBoundOperationActions;
                pendingBindRunnable = null;
                notYetBoundOperationActions = null;
                for (OperationAction action : actions)
                    bindOperationAction(action);
            });
        }
        return this;
    }

    public ObservableValue<OperationAction> operationActionProperty(Object operationCode) {
        ObjectProperty<OperationAction> operationActionProperty = new SimpleObjectProperty<>();
        if (operationActionProperties == null)
            operationActionProperties = new HashMap<>();
        operationActionProperties.put(operationCode, operationActionProperty);
        return operationActionProperty;
    }

    public ObservableBooleanValue authorizedOperationActionProperty(Object operationCode, ObservableValue userPrincipalProperty, AsyncFunction<Object, Boolean> authorizationFunction) {
        // Note: it's possible we don't know yet what operation action we are talking about at this stage, because this
        // method can (and usually is) called before the operation action associated with that code is registered
        Function<OperationAction, Object> operationRequestFactory = this::newOperationActionRequest; // Will return null until the operation action with that code is registered
        // We embed the authorization function to handle the special case where the request is null
        AsyncFunction<Object, Boolean> embedAuthorizationFunction = new AsyncFunction<Object, Boolean>() { // Using a lambda expression here causes a wrong GWT code factorization which lead to an application crash! Keeping the Java 7 style solves the problem.
            @Override
            public Future<Boolean> apply(Object request) {
                if (request != null)
                    return authorizationFunction.apply(request);
                // If the request is null, this is because no operation action with that code has yet been registered, so we
                // don't know what operation it is yet, so we return not authorized by default (if this action is shown in a
                // button, the button will be invisible (or at least disabled) until the operation action is registered
                return Future.succeededFuture(false);
            }
        };
        return AuthorizationUtil.authorizedOperationProperty(
                  operationRequestFactory
                , embedAuthorizationFunction
                , operationActionProperty(operationCode) // will change when operation action will be registered, causing a new authorization evaluation
                , userPrincipalProperty);
    }


    public Action getOperationActionFromClass(Class operationRequestClass) {
        return operationActions.get(operationRequestClass);
    }

    public Action getOperationActionFromCode(Object operationCode) {
        return operationActions.get(operationCode);
    }

    private Object getOperationCodeFromAction(Action action) {
        for (Map.Entry<Object, Action> entry : operationActions.entrySet()) {
            if (entry.getValue() == action)
                return entry.getKey();
        }
        return null;
    }

    public Action getOperationActionFromRequest(Object operationRequest) {
        Action operationAction = getOperationActionFromClass(operationRequest.getClass());
        if (operationAction == null && operationRequest instanceof HasOperationCode)
            operationAction = getOperationActionFromCode(((HasOperationCode) operationRequest).getOperationCode());
        return operationAction;
    }

    public <A, R> void bindOperationAction(OperationAction<A, R> operationAction) {
        if (bindOperationActionNow(operationAction))
            return;
        if (notYetBoundOperationActions == null)
            notYetBoundOperationActions = new ArrayList<>();
        notYetBoundOperationActions.add(operationAction);
    }

    private <A, R> boolean bindOperationActionNow(OperationAction<A, R> operationAction) {
        // The binding is possible only if an operation action has been registered
        Action registeredOperationAction = getOperationActionFromRequest(newOperationActionRequest(operationAction));
        // If this is not the case, we return false (can't do the binding now)
        if (registeredOperationAction == null)
            return false;
        if (operationActionProperties != null) {
            Object code = getOperationCodeFromAction(registeredOperationAction);
            if (code != null) {
                ObjectProperty<OperationAction> operationActionProperty = operationActionProperties.remove(code);
                if (operationActionProperty != null)
                    operationActionProperty.set(operationAction);
                if (operationActionProperties.isEmpty())
                    operationActionProperties = null;
            }
        }
        ActionBinder.bindWritableActionToAction(operationAction, registeredOperationAction);
        return true;
    }

    private <A, R> A newOperationActionRequest(OperationAction<A, R> operationAction) {
        if (operationAction == null)
            return null;
        Function<ActionEvent, A> operationRequestFactory = operationAction.getOperationRequestFactory();
        if (operationRequestFactory != null)
            return operationRequestFactory.apply(new ActionEvent());
        return null;
    }

}
