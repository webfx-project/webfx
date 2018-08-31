package webfx.framework.operation;

import webfx.util.async.AsyncFunction;
import webfx.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class OperationUtil {

    public static <Rq, Rs> Future<Rs> executeOperation(Rq operationRequest, AsyncFunction<Rq, Rs> operationExecutor) {
        if (operationExecutor == null && operationRequest instanceof HasOperationExecutor)
            operationExecutor = ((HasOperationExecutor) operationRequest).getOperationExecutor();
        if (operationExecutor != null)
            return operationExecutor.apply(operationRequest);
        return Future.failedFuture(new IllegalArgumentException("No executor found for operation request " + operationRequest));
    }


}
