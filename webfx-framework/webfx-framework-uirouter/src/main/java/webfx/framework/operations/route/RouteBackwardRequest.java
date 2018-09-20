package webfx.framework.operations.route;

import webfx.framework.operation.HasOperationCode;
import webfx.platforms.core.services.windowhistory.spi.BrowsingHistory;
import webfx.platforms.core.util.async.AsyncFunction;

/**
 * @author Bruno Salmon
 */
public final class RouteBackwardRequest
        extends RouteRequestBase<RouteBackwardRequest>
        implements HasOperationCode {

    private static final String OPERATION_CODE = "RouteBackward";

    public RouteBackwardRequest(BrowsingHistory history) {
        super(history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }

    @Override
    public AsyncFunction<RouteBackwardRequest, Void> getOperationExecutor() {
        return RouteBackwardExecutor::executeRequest;
    }
}
