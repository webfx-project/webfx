package naga.framework.ui.router;

import naga.framework.operation.HasOperationCode;
import naga.platform.client.url.history.History;
import naga.util.async.AsyncFunction;

/**
 * @author Bruno Salmon
 */
public final class RouteBackwardRequest
        extends UiRouteRequest<RouteBackwardRequest>
        implements HasOperationCode {

    private static final String OPERATION_CODE = "BACKWARD";

    public RouteBackwardRequest(History history) {
        super(history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }

    @Override
    public AsyncFunction<RouteBackwardRequest, Void> getOperationExecutor() {
        return request -> {
            request.getHistory().goBack();
            return null;
        };
    }
}
