package mongoose.operations.backend.route;

import mongoose.activities.backend.monitor.MonitorRouting;
import naga.framework.operation.HasOperationCode;
import naga.framework.operations.route.PushRouteRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class RouteToMonitorRequest extends PushRouteRequest implements HasOperationCode {

    private final static String OPERATION_CODE = "MONITOR_ROUTING";

    public RouteToMonitorRequest(History history) {
        super(MonitorRouting.getPath(), history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }
}
