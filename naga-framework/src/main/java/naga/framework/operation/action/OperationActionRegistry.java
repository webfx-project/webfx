package naga.framework.operation.action;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import naga.framework.operation.HasOperationCode;
import naga.framework.spi.authz.mixin.AuthorizationUtil;
import naga.framework.ui.action.Action;
import naga.fx.spi.Toolkit;
import naga.util.async.AsyncFunction;
import naga.util.async.Future;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class OperationActionRegistry {

    private static OperationActionRegistry INSTANCE;
    private Collection<OperationAction> notYetBoundOperationActions;
    private Map<Object, ObjectProperty<OperationAction>> operationActionProperties;
    private Runnable pendingBindRunnable;

    public static OperationActionRegistry getInstance() {
        if (INSTANCE == null)
            INSTANCE = new OperationActionRegistry();
        return INSTANCE;
    }

    private final Map<Object, Action> operationActions = new HashMap<>();

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
            Toolkit.get().scheduler().scheduleDeferred(pendingBindRunnable = () -> {
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
        return AuthorizationUtil.authorizedOperationProperty(this::newOperationActionRequest, request -> request == null ? Future.succeededFuture(false) : authorizationFunction.apply(request), operationActionProperty(operationCode), userPrincipalProperty);
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
        Action registeredOperationAction = getOperationActionFromRequest(newOperationActionRequest(operationAction));
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
        operationAction.bindableTextProperty().bind(registeredOperationAction.textProperty());
        operationAction.bindableGraphicProperty().bind(registeredOperationAction.graphicProperty());
        operationAction.bindableDisabledProperty().bind(registeredOperationAction.disabledProperty());
        operationAction.bindableVisibleProperty().bind(registeredOperationAction.visibleProperty());
        return true;
    }

    private <A, R> A newOperationActionRequest(OperationAction<A, R> operationAction) {
        return operationAction == null ? null : operationAction.getOperationRequestFactory().apply(new ActionEvent());
    }

}
