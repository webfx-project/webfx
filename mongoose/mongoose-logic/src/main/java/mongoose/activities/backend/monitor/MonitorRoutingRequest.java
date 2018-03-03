package mongoose.activities.backend.monitor;

import naga.framework.operation.HasOperationCode;
import naga.framework.ui.router.PushRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class MonitorRoutingRequest extends PushRoutingRequest implements HasOperationCode {

    private final static String OPERATION_CODE = "MONITOR_ROUTING";

    public MonitorRoutingRequest(History history) {
        super(MonitorRouting.PATH, history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }
}
