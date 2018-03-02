package naga.framework.operation.authz;

import naga.framework.operation.ChainedOperationExecutor;
import naga.framework.spi.authz.AuthorizationRequest;
import naga.framework.spi.authz.mixin.HasUserPrincipal;
import naga.util.async.AsyncFunction;
import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class AuthorizableOperationExecutor extends ChainedOperationExecutor {

    private final HasUserPrincipal userPrincipalFetcher;

    public AuthorizableOperationExecutor(HasUserPrincipal userPrincipalFetcher, AsyncFunction nextOperationExecutor) {
        super(nextOperationExecutor);
        this.userPrincipalFetcher = userPrincipalFetcher;
    }

    @Override
    public Future apply(Object operationRequest) {
        if (operationRequest instanceof AuthorizationRequest)
            return ((AuthorizationRequest) operationRequest)
                    .setUserPrincipal(userPrincipalFetcher.getUserPrincipal())
                    .isAuthorizedAsync();
        return new AuthorizationRequest<>()
                .setOperationRequest(operationRequest)
                .setUserPrincipal(userPrincipalFetcher.getUserPrincipal())
                .onAuthorizedExecute(() -> super.apply(operationRequest))
                .executeAsync();
    }
}
