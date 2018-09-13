package mongooses.core.operations.backend.route;

import mongooses.core.activities.backend.monitor.MonitorRouting;
import webfx.framework.operation.HasOperationCode;
import webfx.framework.operations.route.RoutePushRequest;
import webfx.platforms.core.services.browsinghistory.spi.BrowsingHistory;

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
