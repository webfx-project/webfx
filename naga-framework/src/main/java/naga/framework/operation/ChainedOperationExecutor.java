package naga.framework.operation;

import naga.util.async.AsyncFunction;
import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class ChainedOperationExecutor implements AsyncFunction {

    private final AsyncFunction nextOperationExecutor;

    public ChainedOperationExecutor(AsyncFunction nextOperationExecutor) {
        this.nextOperationExecutor = nextOperationExecutor;
    }

    public AsyncFunction getNextOperationExecutor() {
        return nextOperationExecutor;
    }

    @Override
    public Future apply(Object operationRequest) {
        return OperationExecutorUtil.executeOperation(operationRequest, nextOperationExecutor);
    }
}
