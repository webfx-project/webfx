package webfx.framework.operations.route;

import webfx.framework.activity.impl.elementals.uiroute.UiRouteActivityContext;
import webfx.framework.operation.HasOperationCode;
import webfx.framework.router.auth.authz.RouteRequest;
import webfx.platforms.core.services.windowhistory.spi.BrowsingHistory;
import webfx.platforms.core.util.async.AsyncFunction;

/**
 * @author Bruno Salmon
 */
public final class RouteForwardRequest
        extends RouteRequestBase<RouteForwardRequest>
        implements HasOperationCode {

    private static final String OPERATION_CODE = "RouteForward";

    public RouteForwardRequest(BrowsingHistory history) {
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

    public static final class ProvidedEmitter implements RouteRequestEmitter {
        @Override
        public RouteRequest instantiateRouteRequest(UiRouteActivityContext context) {
            return new RouteForwardRequest(context.getHistory());
        }
    }

}
