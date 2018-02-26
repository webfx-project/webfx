package naga.framework.operation;

import naga.util.async.AsyncFunction;
import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class OperationDispatcher implements AsyncFunction {

    private final OperationExecutorRegistry registry;

    public OperationDispatcher(OperationExecutorRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Future apply(Object operationRequest) {
        return OperationExecutorUtil.executeOperation(operationRequest, registry.getOperationExecutorFromRequest(operationRequest));
    }
}
