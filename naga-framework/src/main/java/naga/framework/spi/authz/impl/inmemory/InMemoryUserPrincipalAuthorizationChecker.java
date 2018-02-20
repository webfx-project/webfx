package naga.framework.spi.authz.impl.inmemory;

import naga.framework.spi.authz.impl.UserPrincipalAuthorizationChecker;
import naga.util.async.AsyncResult;
import naga.util.async.Future;
import naga.util.async.FutureBroadcaster;
import naga.util.async.Handler;

/**
 * @author Bruno Salmon
 */
public class InMemoryUserPrincipalAuthorizationChecker implements UserPrincipalAuthorizationChecker {

    private final Object userPrincipal;
    protected final InMemoryAuthorizationRuleRegistry ruleRegistry;
    private FutureBroadcaster<?> loadingFutureBroadcaster;

    public InMemoryUserPrincipalAuthorizationChecker(Object userPrincipal, InMemoryAuthorizationRuleRegistry ruleRegistry) {
        this.userPrincipal = userPrincipal;
        this.ruleRegistry = ruleRegistry;
    }

    @Override
    public Object getUserPrincipal() {
        return userPrincipal;
    }

    @Override
    public Future<Boolean> isAuthorized(Object operationAuthorizationRequest) {
        Future<Boolean> future = Future.future();
        FutureBroadcaster<?> loader = loadingFutureBroadcaster;
        return loader == null ? // means the rules are already loaded, so we can evaluate them and return the result immediately
                evaluateRulesAndComplete(operationAuthorizationRequest, future)
                // Otherwise, we first need to wait the rules to be loaded and only then we can evaluate them and return the result
                : loader.newClient().compose(result -> evaluateRulesAndComplete(operationAuthorizationRequest, future), future);
    }

    private Future<Boolean> evaluateRulesAndComplete(Object operationAuthorizationRequest, Future<Boolean> future) {
        future.complete(ruleRegistry.doesRulesAuthorize(operationAuthorizationRequest));
        return future;
    }

    protected <T> void setUpInMemoryAsyncLoading(Future<T> loadingFuture, Handler<AsyncResult<T>> loadedHandler) {
        FutureBroadcaster<T> broadcaster = new FutureBroadcaster<>(loadingFuture);
        loadingFutureBroadcaster = broadcaster;
        broadcaster.newClient().setHandler(loadedHandler);
        broadcaster.newClient().setHandler(ar -> loadingFutureBroadcaster = null);
    }
}
