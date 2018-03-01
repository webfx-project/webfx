package naga.framework.operation.action;

import javafx.event.ActionEvent;
import naga.util.async.AsyncFunction;
import naga.util.function.Factory;
import naga.util.function.Function;

/**
 * @author Bruno Salmon
 */
public interface OperationActionProducer {

    default AsyncFunction getTopOperationExecutor() {
        return null;
    }

    default OperationActionRegistry getOperationActionRegistry() {
        return OperationActionRegistry.getInstance();
    }

    default <O, R> OperationAction newAction(Factory<O> operationRequestFactory) {
        return newAction(operationRequestFactory, getTopOperationExecutor());
    }

    default <O, R> OperationAction newAction(Factory<O> operationRequestFactory, AsyncFunction<O, R> topOperationExecutor) {
        return initOperationAction(new OperationAction<>(operationRequestFactory, topOperationExecutor));
    }

    default <O, R> OperationAction newAction(Function<ActionEvent, O> operationRequestFactory) {
        return newAction(operationRequestFactory, getTopOperationExecutor());
    }

    default <O, R> OperationAction newAction(Function<ActionEvent, O> operationRequestFactory, AsyncFunction<O, R> topOperationExecutor) {
        return initOperationAction(new OperationAction<>(operationRequestFactory, topOperationExecutor));
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
