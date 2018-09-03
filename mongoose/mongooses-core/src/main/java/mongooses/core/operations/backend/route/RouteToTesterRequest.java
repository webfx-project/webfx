package mongooses.core.operations.backend.route;

import mongooses.core.activities.backend.loadtester.LoadTesterRouting;
import webfx.framework.operation.HasOperationCode;
import webfx.framework.operations.route.RoutePushRequest;
import webfx.platforms.core.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public final class RouteToTesterRequest extends RoutePushRequest implements HasOperationCode {

    private final static String OPERATION_CODE = "RouteToTester";

    public RouteToTesterRequest(History history) {
        super(LoadTesterRouting.getPath(), history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }
}
