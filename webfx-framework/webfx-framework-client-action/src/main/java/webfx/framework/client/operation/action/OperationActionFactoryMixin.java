package webfx.framework.client.operation.action;

import javafx.event.ActionEvent;
import webfx.framework.client.ui.action.Action;
import webfx.framework.client.ui.action.ActionGroup;
import webfx.framework.client.ui.action.ActionGroupBuilder;
import webfx.framework.client.ui.action.impl.SeparatorAction;
import webfx.framework.shared.operation.HasOperationExecutor;
import webfx.framework.shared.operation.OperationUtil;
import webfx.platform.shared.util.async.AsyncFunction;
import webfx.platform.shared.util.async.Future;
import webfx.platform.shared.util.function.Factory;

import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
public interface OperationActionFactoryMixin extends HasOperationExecutor {

    default AsyncFunction getOperationExecutor() {
        return null;
    }

    default <Rq, Rs> Future<Rs> executeOperation(Rq operationRequest) {
        return OperationUtil.executeOperation(operationRequest, getOperationExecutor());
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

    default Action newSeparatorAction() {
        return new SeparatorAction();
    }

    default ActionGroup newActionGroup(Action... actions) {
        return newActionGroup(null, false, actions);
    }

    default ActionGroup newSeparatorActionGroup(Action... actions) {
        return newActionGroup(null, true, actions);
    }

    default ActionGroup newSeparatorActionGroup(String text, Action... actions) {
        return newActionGroup(text, true, actions);
    }

    default ActionGroup newActionGroup(String text, boolean hasSeparators, Action... actions) {
        return new ActionGroupBuilder().setText(text).setActions(actions).setHasSeparators(hasSeparators).build();
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
