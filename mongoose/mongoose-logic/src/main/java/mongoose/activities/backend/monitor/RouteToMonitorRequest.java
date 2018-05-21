package mongoose.activities.backend.monitor;

import naga.framework.operation.HasOperationCode;
import naga.framework.ui.router.PushRouteRequest;
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
