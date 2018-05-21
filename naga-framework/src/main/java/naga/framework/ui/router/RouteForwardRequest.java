package naga.framework.ui.router;

import naga.framework.operation.HasOperationCode;
import naga.platform.client.url.history.History;
import naga.util.async.AsyncFunction;

/**
 * @author Bruno Salmon
 */
public final class RouteForwardRequest
        extends UiRouteRequest<RouteForwardRequest>
        implements HasOperationCode {

    private static final String OPERATION_CODE = "FORWARD";

    public RouteForwardRequest(History history) {
        super(history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }

    @Override
    public AsyncFunction<RouteForwardRequest, Void> getOperationExecutor() {
        return request -> {
            request.getHistory().goForward();
            return null;
        };
    }
}
