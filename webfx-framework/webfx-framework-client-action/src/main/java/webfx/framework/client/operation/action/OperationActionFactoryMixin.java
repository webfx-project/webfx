package webfx.framework.client.operation.action;

import javafx.event.ActionEvent;
import webfx.framework.client.ui.action.Action;
import webfx.framework.client.ui.action.ActionGroup;
import webfx.framework.client.ui.action.ActionGroupBuilder;
import webfx.framework.shared.operation.HasOperationExecutor;
import webfx.platform.shared.util.async.AsyncFunction;
import webfx.platform.shared.util.function.Factory;

import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
public interface OperationActionFactoryMixin extends HasOperationExecutor {

    default AsyncFunction getOperationExecutor() {
        return null;
    }

    default OperationActionRegistry getOperationActionRegistry() {
        return OperationActionRegistry.getInstance();
    }

    default <Rq> OperationAction newAction(Factory<Rq> operationRequestFactory) {
        return newAction(operationRequestFactory, getOperationExecutor());
    }

    default <Rq, Rs> OperationAction newAction(Factory<Rq> operationRequestFactory, AsyncFunction<Rq, Rs> topOperationExecutor) {
        return initOperationAction(new OperationAction<>(operationRequestFactory, topOperationExecutor));
    }

    default <Rq> OperationAction newAction(Function<ActionEvent, Rq> operationRequestFactory) {
        return newAction(operationRequestFactory, getOperationExecutor());
    }

    default <Rq, Rs> OperationAction newAction(Function<ActionEvent, Rq> operationRequestFactory, AsyncFunction<Rq, Rs> topOperationExecutor) {
        return initOperationAction(new OperationAction<>(operationRequestFactory, topOperationExecutor));
    }

    default ActionGroup newActionGroup(Action... actions) {
        return new ActionGroupBuilder().setActions(actions).build();
    }

    default OperationAction initOperationAction(OperationAction operationAction) {
        OperationActionRegistry registry = operationAction.getOperationActionRegistry();
        if (registry == null) {
            registry = getOperationActionRegistry();
            if (registry != null)
                operationAction.setOperationActionRegistry(registry);
        }
        return operationAction;
    }

}
