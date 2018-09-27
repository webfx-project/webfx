package mongooses.core.backend.operations.loadtester;

import mongooses.core.backend.activities.loadtester.LoadTesterRouting;
import webfx.framework.activity.impl.elementals.uiroute.UiRouteActivityContext;
import webfx.framework.operation.HasOperationCode;
import webfx.framework.operations.route.RoutePushRequest;
import webfx.framework.operations.route.RouteRequestEmitter;
import webfx.framework.router.auth.authz.RouteRequest;
import webfx.platforms.core.services.windowhistory.spi.BrowsingHistory;

/**
 * @author Bruno Salmon
 */
public final class RouteToTesterRequest extends RoutePushRequest implements HasOperationCode {

    private final static String OPERATION_CODE = "RouteToTester";

    public RouteToTesterRequest(BrowsingHistory history) {
        super(LoadTesterRouting.getPath(), history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }

    public static final class ProvidedEmitter implements RouteRequestEmitter {
        @Override
        public RouteRequest instantiateRouteRequest(UiRouteActivityContext context) {
            return new RouteToTesterRequest(context.getHistory());
        }
    }
}
