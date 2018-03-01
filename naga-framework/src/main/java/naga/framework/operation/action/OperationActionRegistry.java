package naga.framework.operation.action;

import javafx.event.ActionEvent;
import naga.framework.operation.HasOperationCode;
import naga.framework.ui.action.Action;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class OperationActionRegistry {

    private static OperationActionRegistry INSTANCE;

    public static OperationActionRegistry getInstance() {
        if (INSTANCE == null)
            INSTANCE = new OperationActionRegistry();
        return INSTANCE;
    }

    private final Map<Object, Action> operationActions = new HashMap<>();

    public <A> OperationActionRegistry registerOperationAction(Class<A> operationRequestClass, Action operationAction) {
        operationActions.put(operationRequestClass, operationAction);
        return this;
    }

    public OperationActionRegistry registerOperationAction(Object operationCode, Action operationAction) {
        operationActions.put(operationCode, operationAction);
        return this;
    }

    public Action getOperationActionFromClass(Class operationRequestClass) {
        return operationActions.get(operationRequestClass);
    }

    public Action getOperationActionFromCode(Object operationCode) {
        return operationActions.get(operationCode);
    }

    public Action getOperationActionFromRequest(Object operationRequest) {
        Action operationAction = getOperationActionFromClass(operationRequest.getClass());
        if (operationAction == null && operationRequest instanceof HasOperationCode)
            operationAction = getOperationActionFromCode(((HasOperationCode) operationRequest).getOperationCode());
        return operationAction;
    }

    public <A, R> void bindOperationAction(OperationAction<A, R> operationAction) {
        bindOperationActionNow(operationAction);
    }

    private <A, R> boolean bindOperationActionNow(OperationAction<A, R> operationAction) {
        A operationRequest = operationAction.getOperationRequestFactory().apply(new ActionEvent());
        Action registeredOperationAction = getOperationActionFromRequest(operationRequest);
        if (registeredOperationAction == null)
            return false;
        operationAction.bindableTextProperty().bind(registeredOperationAction.textProperty());
        operationAction.bindableGraphicProperty().bind(registeredOperationAction.graphicProperty());
        operationAction.bindableDisabledProperty().bind(registeredOperationAction.disabledProperty());
        operationAction.bindableVisibleProperty().bind(registeredOperationAction.visibleProperty());
        return true;
    }

}
