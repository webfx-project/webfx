package webfx.framework.client.ui.action.operation;

import javafx.beans.value.ObservableValue;
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

    // OperationAction factory methods

    default <Rq> OperationAction newOperationAction(Factory<Rq> operationRequestFactory, ObservableValue... graphicalDependencies) {
        return newOperationAction(operationRequestFactory, getOperationExecutor(), graphicalDependencies);
    }

    default <Rq, Rs> OperationAction newOperationAction(Factory<Rq> operationRequestFactory, AsyncFunction<Rq, Rs> topOperationExecutor, ObservableValue... graphicalDependencies) {
        return initOperationAction(new OperationAction<>(operationRequestFactory, topOperationExecutor, graphicalDependencies));
    }

    // Same but with an action event passed to the operation request factory

    default <Rq> OperationAction newOperationAction(Function<ActionEvent, Rq> operationRequestFactory, ObservableValue... graphicalDependencies) {
        return newOperationAction(operationRequestFactory, getOperationExecutor(), graphicalDependencies);
    }

    default <Rq, Rs> OperationAction newOperationAction(Function<ActionEvent, Rq> operationRequestFactory, AsyncFunction<Rq, Rs> topOperationExecutor, ObservableValue... graphicalDependencies) {
        return initOperationAction(new OperationAction<>(operationRequestFactory, topOperationExecutor, graphicalDependencies));
    }

    // Action group factory methods

    default Action newSeparatorAction() {
        return new SeparatorAction();
    }

    default ActionGroup newActionGroup(Action... actions) {
        return newActionGroup(null, false, actions);
    }

    default ActionGroup newSeparatorActionGroup(Action... actions) {
        return newActionGroup(null, true, actions);
    }

    default ActionGroup newSeparatorActionGroup(Object i18nKey, Action... actions) {
        return newActionGroup(i18nKey, true, actions);
    }

    default ActionGroup newActionGroup(Object i18nKey, boolean hasSeparators, Action... actions) {
        return new ActionGroupBuilder().setI18nKey(i18nKey).setActions(actions).setHasSeparators(hasSeparators).build();
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
