package webfx.framework.shared.services.authz.spi.impl.inmemory;

import webfx.framework.shared.services.authz.spi.impl.UserPrincipalAuthorizationChecker;
import webfx.platform.shared.util.async.AsyncResult;
import webfx.platform.shared.util.async.Future;
import webfx.platform.shared.util.async.FutureBroadcaster;
import webfx.platform.shared.util.async.Handler;

/**
 * @author Bruno Salmon
 */
public class InMemoryUserPrincipalAuthorizationChecker implements UserPrincipalAuthorizationChecker {

    private final Object userPrincipal;
    protected final InMemoryAuthorizationRuleRegistry ruleRegistry;
    private FutureBroadcaster<?> rulesLoadingBroadcaster;

    public InMemoryUserPrincipalAuthorizationChecker(Object userPrincipal) {
        this(userPrincipal, new InMemoryAuthorizationRuleRegistry());
    }

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
        FutureBroadcaster<?> loader = rulesLoadingBroadcaster;
        return loader == null ? // means the rules are already loaded, so we can evaluate them and return the result immediately
                Future.succeededFuture(ruleRegistry.doesRulesAuthorize(operationAuthorizationRequest))
                // Otherwise, we first need to wait the rules to be loaded and only then we can evaluate them and return the result
                : loader.newClient().compose((result, finalFuture) -> finalFuture.complete(ruleRegistry.doesRulesAuthorize(operationAuthorizationRequest)));
    }

    protected <T> void setUpInMemoryAsyncRulesLoading(Future<T> loadingFuture, Handler<AsyncResult<T>> loadedHandler) {
        FutureBroadcaster<T> broadcaster = new FutureBroadcaster<>(loadingFuture);
        rulesLoadingBroadcaster = broadcaster;
        broadcaster.newClient().setHandler(loadedHandler);
        broadcaster.newClient().setHandler(ar -> rulesLoadingBroadcaster = null);
    }
}
