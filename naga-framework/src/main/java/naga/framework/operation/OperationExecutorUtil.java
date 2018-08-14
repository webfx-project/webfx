package naga.framework.operation;

import naga.framework.operation.action.ChainedActionOperationExecutor;
import naga.framework.operation.action.OperationActionRegistry;
import naga.framework.operation.authz.AuthorizableOperationExecutor;
import naga.framework.services.authz.mixin.HasUserPrincipal;
import naga.util.async.AsyncFunction;
import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class OperationExecutorUtil {

    public static <Rq, Rs> Future<Rs> executeOperation(Rq operationRequest, AsyncFunction<Rq, Rs> operationExecutor) {
        if (operationExecutor == null && operationRequest instanceof HasOperationExecutor)
            operationExecutor = ((HasOperationExecutor) operationRequest).getOperationExecutor();
        if (operationExecutor != null)
            return operationExecutor.apply(operationRequest);
        return Future.failedFuture(new IllegalArgumentException("No executor found for operation request " + operationRequest));
    }

    public static ChainedActionOperationExecutor createAuthorizableOperationActionExecutor(OperationActionRegistry operationActionRegistry, HasUserPrincipal userPrincipalFetcher, OperationExecutorRegistry operationExecutorRegistry) {
        return new ChainedActionOperationExecutor(operationActionRegistry,
               new AuthorizableOperationExecutor(userPrincipalFetcher,
               new OperationDispatcher(operationExecutorRegistry)));
    }


}
