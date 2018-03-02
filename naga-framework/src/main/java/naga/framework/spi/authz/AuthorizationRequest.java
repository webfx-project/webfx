package naga.framework.spi.authz;

import naga.util.async.AsyncFunction;
import naga.util.async.Future;
import naga.util.function.Consumer;
import naga.util.function.Function;
import naga.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public final class AuthorizationRequest<O, R> {

    private Object userPrincipal;
    private O operationRequest;
    private AsyncFunction<O, R> authorizedOperationAsyncExecutor;
    private AsyncFunction<Throwable, ?> unauthorizedOperationAsyncExecutor;
    private AuthorizationServiceProvider provider;

    public Object getUserPrincipal() {
        return userPrincipal;
    }

    public AuthorizationRequest<O, R> setUserPrincipal(Object userPrincipal) {
        this.userPrincipal = userPrincipal;
        return this;
    }

    public O getOperationRequest() {
        return operationRequest;
    }

    public AuthorizationRequest<O, R> setOperationRequest(O operationRequest) {
        this.operationRequest = operationRequest;
        return this;
    }

    public AsyncFunction<O, R> getAuthorizedOperationAsyncExecutor() {
        return authorizedOperationAsyncExecutor;
    }

    public AuthorizationRequest<O, R> onAuthorizedExecuteAsync(AsyncFunction<O, R> authorizedExecutor) {
        this.authorizedOperationAsyncExecutor = authorizedExecutor;
        return this;
    }

    public AuthorizationRequest<O, R> onAuthorizedExecute(Function<O, R> authorizedExecutor) {
        return onAuthorizedExecuteAsync((O o) -> Future.succeededFuture(authorizedExecutor.apply(o)));
    }

    public AuthorizationRequest<O, R> onAuthorizedExecute(Consumer<O> authorizedExecutor) {
        return onAuthorizedExecuteAsync(o -> Future.consumeAsync(authorizedExecutor, o));
    }

    public AuthorizationRequest<O, R> onAuthorizedExecute(Runnable authorizedExecutor) {
        return onAuthorizedExecuteAsync(o -> Future.runAsync(authorizedExecutor));
    }

    public AsyncFunction<Throwable, ?> getUnauthorizedOperationAsyncExecutor() {
        return unauthorizedOperationAsyncExecutor;
    }

    public AuthorizationRequest<O, R> onUnauthorizedExecuteAsync(AsyncFunction<Throwable, ?> unauthorizedAsyncExecutor) {
        this.unauthorizedOperationAsyncExecutor = unauthorizedAsyncExecutor;
        return this;
    }

    public AuthorizationRequest<O, R> onUnauthorizedExecute(Consumer<Throwable> authorizedExecutor) {
        return onUnauthorizedExecuteAsync(o -> Future.consumeAsync(authorizedExecutor, o));
    }

    public AuthorizationRequest<O, R> onUnauthorizedExecute(Runnable authorizedExecutor) {
        return onUnauthorizedExecuteAsync(o -> Future.runAsync(authorizedExecutor));
    }

    public AuthorizationServiceProvider getProvider() {
        return provider;
    }

    public AuthorizationRequest<O, R> setProvider(AuthorizationServiceProvider provider) {
        this.provider = provider;
        return this;
    }

    public AuthorizationRequest<O, R> complete() {
        if (unauthorizedOperationAsyncExecutor == null)
            unauthorizedOperationAsyncExecutor = o -> Future.failedFuture(new UnauthorizedOperationException());
        if (provider == null)
            setProvider(ServiceLoaderHelper.loadService(AuthorizationServiceProvider.class));
        return this;
    }

    public Future<Boolean> isAuthorizedAsync() {
        return complete().getProvider().isAuthorized(getOperationRequest(), getUserPrincipal());
    }

    public Future<R> executeAsync() {
        Future<R> future = Future.future();
        isAuthorizedAsync().setHandler(ar -> {
            if (ar.succeeded() && ar.result())
                getAuthorizedOperationAsyncExecutor().apply(getOperationRequest()).setHandler(future.completer());
            else
                getUnauthorizedOperationAsyncExecutor().apply(ar.cause()).setHandler(ar2 -> future.fail(ar.cause()));
        });
        return future;
    }

}
