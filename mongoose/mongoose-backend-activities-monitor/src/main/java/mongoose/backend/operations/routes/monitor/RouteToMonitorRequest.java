package mongoose.backend.operations.routes.monitor;

import mongoose.backend.activities.monitor.routing.MonitorRouting;
import webfx.framework.shared.operation.HasOperationCode;
import webfx.framework.client.operations.route.RoutePushRequest;
import webfx.platform.client.services.windowhistory.spi.BrowsingHistory;

/**
 * @author Bruno Salmon
 */
public final class RouteToMonitorRequest extends RoutePushRequest implements HasOperationCode {

    private final static String OPERATION_CODE = "RouteToMonitor";

    public RouteToMonitorRequest(BrowsingHistory history) {
        super(MonitorRouting.getPath(), history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }

}
