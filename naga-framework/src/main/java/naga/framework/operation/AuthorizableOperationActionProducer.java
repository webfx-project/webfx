package naga.framework.operation;

import naga.framework.spi.authz.AuthorizationRequest;
import naga.framework.ui.session.HasUserPrincipal;
import naga.util.async.AsyncFunction;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public interface AuthorizableOperationActionProducer extends OperationActionProducer, HasUserPrincipal {

    Map<HasUserPrincipal, AsyncFunction> topOperationExecutors = new HashMap<>();

    default OperationExecutorRegistry getOperationExecutorRegistry() {
        return OperationExecutorRegistry.getInstance();
    }

    @Override
    default AsyncFunction getTopOperationExecutor() {
        AsyncFunction executor = topOperationExecutors.get(this);
        if (executor == null)
            topOperationExecutors.put(this, executor = createAuthorizableOperationActionExecutor());
        return executor;
    }

    default AsyncFunction createAuthorizableOperationActionExecutor() {
        return OperationExecutorUtil.createAuthorizableOperationActionExecutor(getOperationActionRegistry(), this::newAuthorizationRequest, getOperationExecutorRegistry());
    }

    default AuthorizationRequest newAuthorizationRequest() {
        return new AuthorizationRequest().setUserPrincipal(getUserPrincipal());
    }

}
