package naga.framework.operation.authz;

import naga.framework.operation.ChainedOperationExecutor;
import naga.framework.spi.authz.AuthorizationRequest;
import naga.util.async.AsyncFunction;
import naga.util.async.Future;
import naga.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public class AuthorizableOperationExecutor extends ChainedOperationExecutor {

    private final Factory<AuthorizationRequest> authorizationRequestFactory;

    public AuthorizableOperationExecutor(Factory<AuthorizationRequest> authorizationRequestFactory, AsyncFunction nextOperationExecutor) {
        super(nextOperationExecutor);
        this.authorizationRequestFactory = authorizationRequestFactory;
    }

    @Override
    public Future apply(Object operationRequest) {
        return authorizationRequestFactory.create()
                .setOperationRequest(operationRequest)
                .onAuthorizedExecute(() -> super.apply(operationRequest))
                .executeAsync();
    }

    public Future<Boolean> isOperationAuthorized(Object operationRequest) {
        return authorizationRequestFactory.create()
                .setOperationRequest(operationRequest)
                .isAuthorizedAsync();
    }
}
