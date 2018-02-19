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
    protected final InMemoryAuthorizationRule inMemoryAuthorizationRules;
    private FutureBroadcaster<?> loadingFutureBroadcaster;

    public InMemoryUserPrincipalAuthorizationChecker(Object userPrincipal, InMemoryAuthorizationRule inMemoryAuthorizationRules) {
        this.userPrincipal = userPrincipal;
        this.inMemoryAuthorizationRules = inMemoryAuthorizationRules;
    }

    @Override
    public Object getUserPrincipal() {
        return userPrincipal;
    }

    @Override
    public Future<Boolean> isAuthorized(Object operationAuthorizationRequest) {
        Future<Boolean> future = Future.future();
        FutureBroadcaster<?> broadcaster = loadingFutureBroadcaster;
        return broadcaster == null ? complete(operationAuthorizationRequest, future)
                : broadcaster.newClient().compose(result -> complete(operationAuthorizationRequest, future), future);
    }

    private Future<Boolean> complete(Object operationAuthorizationRequest, Future<Boolean> future) {
        future.complete(inMemoryAuthorizationRules.authorizes(operationAuthorizationRequest));
        return future;
    }

    protected <T> void setUpInMemoryAsyncLoading(Future<T> loadingFuture, Handler<AsyncResult<T>> loadedHandler) {
        FutureBroadcaster<T> broadcaster = new FutureBroadcaster<>(loadingFuture);
        loadingFutureBroadcaster = broadcaster;
        broadcaster.newClient().setHandler(loadedHandler);
        broadcaster.newClient().setHandler(ar -> loadingFutureBroadcaster = null);
    }
}
