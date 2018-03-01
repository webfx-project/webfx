package naga.framework.operation;

import naga.framework.operation.action.ChainedActionOperationExecutor;
import naga.framework.operation.action.OperationActionRegistry;
import naga.framework.operation.authz.AuthorizableOperationExecutor;
import naga.framework.spi.authz.AuthorizationRequest;
import naga.util.async.AsyncFunction;
import naga.util.async.Future;
import naga.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public class OperationExecutorUtil {

    public static Future executeOperation(Object operationRequest, AsyncFunction operationExecutor) {
        if (operationExecutor == null && operationRequest instanceof HasOperationExecutor)
            operationExecutor = ((HasOperationExecutor) operationRequest).getOperationExecutor();
        if (operationExecutor != null)
            return operationExecutor.apply(operationRequest);
        return Future.failedFuture(new IllegalArgumentException("No executor found for operation request " + operationRequest));
    }

    public static ChainedActionOperationExecutor createAuthorizableOperationActionExecutor(OperationActionRegistry operationActionRegistry, Factory<AuthorizationRequest> authorizationRequestFactory, OperationExecutorRegistry operationExecutorRegistry) {
        return new ChainedActionOperationExecutor(operationActionRegistry,
               new AuthorizableOperationExecutor(authorizationRequestFactory,
               new OperationDispatcher(operationExecutorRegistry)));
    }


}
