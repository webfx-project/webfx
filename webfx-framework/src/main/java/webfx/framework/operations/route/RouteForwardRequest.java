package webfx.framework.operations.route;

import webfx.framework.operation.HasOperationCode;
import webfx.platform.client.url.history.History;
import webfx.util.async.AsyncFunction;

/**
 * @author Bruno Salmon
 */
public final class RouteForwardRequest
        extends RouteRequestBase<RouteForwardRequest>
        implements HasOperationCode {

    private static final String OPERATION_CODE = "RouteForward";

    public RouteForwardRequest(History history) {
        super(history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }

    @Override
    public AsyncFunction<RouteForwardRequest, Void> getOperationExecutor() {
        return RouteForwardExecutor::executeRequest;
    }
}
