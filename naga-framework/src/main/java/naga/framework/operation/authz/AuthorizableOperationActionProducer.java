package naga.framework.operation.authz;

import naga.framework.operation.OperationExecutorRegistry;
import naga.framework.operation.OperationExecutorUtil;
import naga.framework.operation.action.OperationActionProducer;
import naga.framework.spi.authz.mixin.AuthorizationFactoryMixin;
import naga.framework.spi.authz.mixin.HasUserPrincipal;
import naga.util.async.AsyncFunction;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public interface AuthorizableOperationActionProducer extends OperationActionProducer, AuthorizationFactoryMixin {

    Map<HasUserPrincipal, AsyncFunction> topOperationExecutors = new HashMap<>();

    default OperationExecutorRegistry getOperationExecutorRegistry() {
        return OperationExecutorRegistry.getInstance();
    }

    @Override
    default AsyncFunction getOperationExecutor() {
        AsyncFunction executor = topOperationExecutors.get(this);
        if (executor == null)
            topOperationExecutors.put(this, executor = createAuthorizableOperationActionExecutor());
        return executor;
    }

    default AsyncFunction createAuthorizableOperationActionExecutor() {
        return OperationExecutorUtil.createAuthorizableOperationActionExecutor(getOperationActionRegistry(), this, getOperationExecutorRegistry());
    }

}
