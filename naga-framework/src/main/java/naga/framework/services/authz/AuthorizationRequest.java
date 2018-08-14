package naga.framework.services.authz;

import naga.framework.services.authz.spi.AuthorizationServiceProvider;
import naga.util.async.AsyncFunction;
import naga.util.async.Future;
import naga.util.function.Consumer;
import naga.util.function.Function;
import naga.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public final class AuthorizationRequest<Rq, Rs> {

    private Object userPrincipal;
    private Rq operationRequest;
    private AsyncFunction<Rq, Rs> authorizedOperationAsyncExecutor;
    private AsyncFunction<Throwable, ?> unauthorizedOperationAsyncExecutor;
    private AuthorizationServiceProvider provider;

    public Object getUserPrincipal() {
        return userPrincipal;
    }

    public AuthorizationRequest<Rq, Rs> setUserPrincipal(Object userPrincipal) {
        this.userPrincipal = userPrincipal;
        return this;
    }

    public Rq getOperationRequest() {
        return operationRequest;
    }

    public AuthorizationRequest<Rq, Rs> setOperationRequest(Rq operationRequest) {
        this.operationRequest = operationRequest;
        return this;
    }

    public AsyncFunction<Rq, Rs> getAuthorizedOperationAsyncExecutor() {
        return authorizedOperationAsyncExecutor;
    }

    public AuthorizationRequest<Rq, Rs> onAuthorizedExecuteAsync(AsyncFunction<Rq, Rs> authorizedExecutor) {
        this.authorizedOperationAsyncExecutor = authorizedExecutor;
        return this;
    }

    public AuthorizationRequest<Rq, Rs> onAuthorizedExecute(Function<Rq, Rs> authorizedExecutor) {
        return onAuthorizedExecuteAsync((Rq rq) -> Future.succeededFuture(authorizedExecutor.apply(rq)));
    }

    public AuthorizationRequest<Rq, Rs> onAuthorizedExecute(Consumer<Rq> authorizedExecutor) {
        return onAuthorizedExecuteAsync(rq -> Future.consumeAsync(authorizedExecutor, rq));
    }

    public AuthorizationRequest<Rq, Rs> onAuthorizedExecute(Runnable authorizedExecutor) {
        return onAuthorizedExecuteAsync(rq -> Future.runAsync(authorizedExecutor));
    }

    public AsyncFunction<Throwable, ?> getUnauthorizedOperationAsyncExecutor() {
        return unauthorizedOperationAsyncExecutor;
    }

    public AuthorizationRequest<Rq, Rs> onUnauthorizedExecuteAsync(AsyncFunction<Throwable, ?> unauthorizedAsyncExecutor) {
        this.unauthorizedOperationAsyncExecutor = unauthorizedAsyncExecutor;
        return this;
    }

    public AuthorizationRequest<Rq, Rs> onUnauthorizedExecute(Consumer<Throwable> authorizedExecutor) {
        return onUnauthorizedExecuteAsync(o -> Future.consumeAsync(authorizedExecutor, o));
    }

    public AuthorizationRequest<Rq, Rs> onUnauthorizedExecute(Runnable authorizedExecutor) {
        return onUnauthorizedExecuteAsync(o -> Future.runAsync(authorizedExecutor));
    }

    public AuthorizationServiceProvider getProvider() {
        return provider;
    }

    public AuthorizationRequest<Rq, Rs> setProvider(AuthorizationServiceProvider provider) {
        this.provider = provider;
        return this;
    }

    public AuthorizationRequest<Rq, Rs> complete() {
        if (unauthorizedOperationAsyncExecutor == null)
            unauthorizedOperationAsyncExecutor = o -> Future.failedFuture(new UnauthorizedOperationException());
        if (provider == null)
            setProvider(ServiceLoaderHelper.loadService(AuthorizationServiceProvider.class));
        return this;
    }

    public Future<Boolean> isAuthorizedAsync() {
        return complete().getProvider().isAuthorized(getOperationRequest(), getUserPrincipal());
    }

    public Future<Rs> executeAsync() {
        Future<Rs> future = Future.future();
        isAuthorizedAsync().setHandler(ar -> {
            if (ar.succeeded() && ar.result())
                getAuthorizedOperationAsyncExecutor().apply(getOperationRequest()).setHandler(future.completer());
            else
                getUnauthorizedOperationAsyncExecutor().apply(ar.cause()).setHandler(ar2 -> future.fail(ar.cause()));
        });
        return future;
    }

}
